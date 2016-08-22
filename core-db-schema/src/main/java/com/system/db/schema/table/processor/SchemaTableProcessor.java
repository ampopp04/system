package com.system.db.schema.table.processor;


import com.system.db.entity.Entity;
import com.system.db.entity.identity.EntityIdentity;
import com.system.db.entity.named.NamedEntity;
import com.system.db.migration.table.TableCreationMigrationCallback;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.schema.datatype.SchemaDataType;
import com.system.db.schema.datatype.SchemaDataTypeRepository;
import com.system.db.schema.table.SchemaTable;
import com.system.db.schema.table.column.SchemaTableColumn;
import com.system.db.schema.table.column.SchemaTableColumnRepository;
import com.system.db.schema.table.column.relationship.SchemaTableColumnRelationship;
import com.system.util.clazz.ClassUtils;
import com.system.util.collection.CollectionUtils;
import com.system.util.validation.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import static com.system.db.entity.identity.EntityIdentity.ID_TYPE_NAME_LOWERCASE;
import static com.system.util.clazz.ClassUtils.getGenericTypeArgument;
import static com.system.util.clazz.ClassUtils.toClassName;

/**
 * The <class>SchemaTableProcessor</class> maintains
 * the state of the schema tables.  This processor is called
 * for any table creations. For each table creation this
 * processor will update the schema tables to reflect
 * the addition of the new table, any columns, and their relationships.
 *
 * @author Andrew
 * @see SchemaTable
 * @see SchemaTableColumn
 * @see SchemaTableColumnRelationship
 * @see SchemaDataType
 */
@Transactional
public class SchemaTableProcessor extends TableCreationMigrationCallback {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    private static final String DEFAULT_DISPLAY_COLUMN_NAME = "name";

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected SchemaDataTypeRepository schemaDataTypeRepository;

    @Autowired
    protected NamedEntityRepository<SchemaTable> schemaTableRepository;

    @Autowired
    protected SchemaTableColumnRepository schemaTableColumnRepository;

    @Autowired
    protected SystemRepository<SchemaTableColumnRelationship> schemaTableColumnRelationshipRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Callback Method                                              //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Callback invoked when new table is created
     *
     * @param tableEntityClass
     */
    @Override
    protected void afterTableCreation(Class<? extends Entity> tableEntityClass) {
        createSchemaTableColumns(createSchemaTable(tableEntityClass), getEntityType(tableEntityClass), true);
    }

    /**
     * Callback invoked when new table is created, this method
     * is called after the first callback is invoked in afterTableCreation
     *
     * @param tableEntityClass
     */
    @Override
    protected void afterTableCreationSecondPass(Class<? extends Entity> tableEntityClass) {
        createSchemaTableColumns(schemaTableRepository.findByName(tableEntityClass.getSimpleName()), getEntityType(tableEntityClass), false);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                Creation Methods                                              //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Create the schema table for this specific entity table
     *
     * @param tableEntityClass
     * @return
     */
    private SchemaTable createSchemaTable(Class<? extends Entity> tableEntityClass) {
        SchemaTable schemaTable = new SchemaTable();
        schemaTable.setName(tableEntityClass.getSimpleName());
        schemaTable.setEntityClass(tableEntityClass.getName());
        return schemaTableRepository.save(schemaTable);
    }

    /**
     * Create all columns represented by entity schema table
     *
     * @param schemaTable
     * @param entityType
     */
    private void createSchemaTableColumns(SchemaTable schemaTable, EntityType<? extends Entity> entityType, boolean firstPass) {
        for (Attribute<?, ?> columnAttribute : CollectionUtils.iterable(entityType.getAttributes())) {
            //Skip collection like properties as they represent side-tables
            if (!columnAttribute.isCollection()) {
                createSchemaTableColumn(schemaTable, entityType, columnAttribute, firstPass);
            }
        }
    }

    /**
     * Create a specific column of this schema table
     *
     * @param schemaTable
     * @param columnAttribute
     */
    private void createSchemaTableColumn(SchemaTable schemaTable, EntityType<? extends Entity> entityType, Attribute<?, ?> columnAttribute, boolean firstPass) {
        if (!firstPass) {
            //Table has been saved therefore it is the second phase, we can now safely populated all fk relationships
            //since we now know they exist
            createSchemaTableColumnRelationships(this.schemaTableColumnRepository.findBySchemaTableNameAndName(schemaTable.getName(), columnAttribute.getName()), columnAttribute);
        } else {
            SchemaTableColumn tableColumn = new SchemaTableColumn();
            tableColumn.setName(columnAttribute.getName());
            tableColumn.setSchemaTable(schemaTable);
            tableColumn.setSchemaDataType(getSchemaDataTypeByColumnAttribute(entityType, columnAttribute));
            this.schemaTableColumnRepository.save(tableColumn);
        }
    }

    /**
     * Retrieve the correct data type from the database for the given column attribute
     *
     * @param entityType
     * @param columnAttribute
     * @return
     */
    private SchemaDataType getSchemaDataTypeByColumnAttribute(EntityType<? extends Entity> entityType, Attribute<?, ?> columnAttribute) {
        SchemaDataType dataType = this.schemaDataTypeRepository.findByJavaType(toClassName(columnAttribute.getJavaType()));
        return dataType != null ? dataType :
                this.schemaDataTypeRepository.findByJavaType(
                        getGenericTypeArgument(entityType.getJavaType(), EntityIdentity.class.getTypeParameters()[0])
                                .getTypeName());
    }

    /**
     * Create the column relationships to designate how specific columns
     * associate to other columns and tables
     *
     * @param tableColumn
     * @param columnAttribute
     */
    private void createSchemaTableColumnRelationships(SchemaTableColumn tableColumn, Attribute<?, ?> columnAttribute) {
        if (columnAttribute.isAssociation()) {
            SchemaTableColumnRelationship columnRelationship = new SchemaTableColumnRelationship();
            columnRelationship.setSchemaTableColumn(tableColumn);
            columnRelationship.setParentSchemaTableColumn(getTableColumnIdByAttributeType(columnAttribute));
            columnRelationship.setParentDisplaySchemaTableColumn(getParentDisplaySchemaTableColumn(tableColumn, columnAttribute));
            this.schemaTableColumnRelationshipRepository.save(columnRelationship);

            tableColumn.setSchemaTableColumnRelationship(columnRelationship);
            this.schemaTableColumnRepository.save(tableColumn);
        }
    }

    private SchemaTableColumn getParentDisplaySchemaTableColumn(SchemaTableColumn tableColumn, Attribute<?, ?> columnAttribute) {
        String columnName = ID_TYPE_NAME_LOWERCASE;
        if (ClassUtils.isAssignable(columnAttribute.getJavaType(), NamedEntity.class)) {
            columnName = DEFAULT_DISPLAY_COLUMN_NAME;
        }
        SchemaTableColumn stc = this.schemaTableColumnRepository.findBySchemaTableNameAndName(tableColumn.getSchemaTable().getName(), columnName);
        return stc;
    }

    /**
     * For a given attribute find the associated schema table column
     * representing that entities id
     *
     * @param columnAttribute
     * @return
     */
    private SchemaTableColumn getTableColumnIdByAttributeType(Attribute<?, ?> columnAttribute) {
        String tableName = ((EntityType) ((SingularAttribute) columnAttribute).getType()).getName();
        SchemaTableColumn tableColumn = this.schemaTableColumnRepository.findBySchemaTableNameAndName(tableName, ID_TYPE_NAME_LOWERCASE);
        ValidationUtils.assertNotNull(tableColumn, "[%s] id schema column not found.", tableName);
        return tableColumn;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                   Getter Methods                                              //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Get the columns attributes for this specific entity
     *
     * @param tableEntityClass
     * @return
     */
    private EntityType<? extends Entity> getEntityType(Class<? extends Entity> tableEntityClass) {
        return entityManager.getMetamodel().entity(tableEntityClass);
    }
}
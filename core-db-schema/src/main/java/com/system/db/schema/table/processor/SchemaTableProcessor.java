package com.system.db.schema.table.processor;


import com.system.db.entity.Entity;
import com.system.db.entity.identity.EntityIdentity;
import com.system.db.migration.table.TableCreationMigrationCallback;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.schema.datatype.SchemaDataType;
import com.system.db.schema.datatype.SchemaDataTypeRepository;
import com.system.db.schema.table.SchemaTable;
import com.system.db.schema.table.SchemaTableRepository;
import com.system.db.schema.table.column.SchemaTableColumn;
import com.system.db.schema.table.column.SchemaTableColumnRepository;
import com.system.db.schema.table.column.relationship.SchemaTableColumnRelationship;
import com.system.util.collection.CollectionUtils;
import com.system.util.validation.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

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

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected SchemaDataTypeRepository schemaDataTypeRepository;

    @Autowired
    protected SchemaTableRepository schemaTableRepository;

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
        createSchemaTableColumns(createSchemaTable(tableEntityClass), getEntityType(tableEntityClass));
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
    private void createSchemaTableColumns(SchemaTable schemaTable, EntityType<? extends Entity> entityType) {
        for (Attribute<?, ?> columnAttribute : CollectionUtils.iterable(entityType.getAttributes())) {
            //Skip collection like properties as they represent side-tables
            if (!columnAttribute.isCollection()) {
                createSchemaTableColumn(schemaTable, entityType, columnAttribute);
            }
        }
    }

    /**
     * Create a specific column of this schema table
     *
     * @param schemaTable
     * @param columnAttribute
     */
    private void createSchemaTableColumn(SchemaTable schemaTable, EntityType<? extends Entity> entityType, Attribute<?, ?> columnAttribute) {
        SchemaTableColumn tableColumn = new SchemaTableColumn();
        tableColumn.setName(columnAttribute.getName());
        tableColumn.setSchemaTable(schemaTable);
        tableColumn.setSchemaDataType(getSchemaDataTypeByColumnAttribute(entityType, columnAttribute));
        createSchemaTableColumnRelationships(this.schemaTableColumnRepository.save(tableColumn), columnAttribute);
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
            this.schemaTableColumnRelationshipRepository.save(columnRelationship);
        }
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
        SchemaTableColumn tableColumn = this.schemaTableColumnRepository.findBySchemaTableNameAndName(tableName, "id");
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
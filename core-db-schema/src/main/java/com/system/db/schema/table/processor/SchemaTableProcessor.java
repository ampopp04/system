package com.system.db.schema.table.processor;


import com.system.db.entity.Entity;
import com.system.db.entity.identity.EntityIdentity;
import com.system.db.entity.named.NamedEntity;
import com.system.db.migration.table.TableCreationMigrationCallback;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.schema.datatype.SchemaDataType;
import com.system.db.schema.datatype.SchemaDataTypeRepository;
import com.system.db.schema.datatype.SchemaDataTypes;
import com.system.db.schema.table.SchemaTable;
import com.system.db.schema.table.column.SchemaTableColumn;
import com.system.db.schema.table.column.SchemaTableColumnRepository;
import com.system.db.schema.table.column.relationship.SchemaTableColumnRelationship;
import com.system.util.clazz.ClassUtils;
import com.system.util.collection.CollectionUtils;
import com.system.util.validation.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
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

    //Autowired on Setter to avoid circular injection dependency
    protected ApplicationContext applicationContext;

    @Lazy
    @Autowired
    protected SchemaDataTypeRepository schemaDataTypeRepository;

    @Lazy
    @Autowired
    protected NamedEntityRepository<SchemaTable> schemaTableRepository;

    @Lazy
    @Autowired
    protected SchemaTableColumnRepository schemaTableColumnRepository;

    @Lazy
    @Autowired
    protected SystemRepository<SchemaTableColumnRelationship, Integer> schemaTableColumnRelationshipRepository;

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
        int columnCounter = 1;
        for (Attribute<?, ?> columnAttribute : CollectionUtils.iterable(entityType.getAttributes())) {
            //Skip collection like properties as they represent side-tables
            if (!columnAttribute.isCollection()) {
                createSchemaTableColumn(schemaTable, entityType, columnAttribute, firstPass, columnCounter);
                columnCounter++;
            }
        }
    }

    /**
     * Create a specific column of this schema table
     *
     * @param schemaTable
     * @param columnAttribute
     */
    private void createSchemaTableColumn(SchemaTable schemaTable, EntityType<? extends Entity> entityType, Attribute<?, ?> columnAttribute, boolean firstPass, Integer defaultColumnOrder) {
        if (!firstPass) {
            //Table has been saved therefore it is the second phase, we can now safely populated all fk relationships
            //since we now know they exist
            createSchemaTableColumnRelationships(this.schemaTableColumnRepository.findBySchemaTableNameAndName(schemaTable.getName(), columnAttribute.getName()), columnAttribute);
        } else {
            SchemaTableColumn tableColumn = new SchemaTableColumn();
            tableColumn.setName(columnAttribute.getName());
            tableColumn.setSchemaTable(schemaTable);
            tableColumn.setSchemaDataType(getSchemaDataTypeByColumnAttribute(entityType, columnAttribute));
            tableColumn.setDefaultColumnOrder(defaultColumnOrder);
            tableColumn.setDisplayHidden(false);
            setColumnUiDefaultConfigurations(tableColumn);
            this.schemaTableColumnRepository.save(tableColumn);
        }
    }

    private void setColumnUiDefaultConfigurations(SchemaTableColumn tableColumn) {

        //If this is an FK field column then it is handled as a special case.
        if (tableColumn.getSchemaTableColumnRelationship() != null) {
            return;
        }

        String dataTypeName = tableColumn.getSchemaDataType().getName();

        if (SchemaDataTypes.NUMBER.isType(dataTypeName)) {

            //Set the extra configuration for the grid column related to this table column
            tableColumn.setUiColumnConfiguration("{xtype: 'numbercolumn', format:'0', filter: {type: 'number',hideTrigger: true,keyNavEnabled: false,mouseWheelEnabled: false}}");

            //Set the extra configuration for the field used in forms and the UI model related to this table column
            tableColumn.setUiFieldConfiguration("{xtype: 'numberfield',hideTrigger: true,keyNavEnabled: false,mouseWheelEnabled: false,decimalPrecision:0}");

            /*if ("id".equals(tableColumn.getName()) || schemaTableRepository.findByName(StringUtils.capitalize(tableColumn.getName())) != null) {
                return;
            }

            String type = "number";

            if ("Integer".equals(dataTypeName)) {
                type = "int";
            }

            //Set the extra configuration for the model field used in the UI data models
            tableColumn.setUiModelFieldConfiguration("{type: '" + type + "', allowNull: true}");*/

        } else if (SchemaDataTypes.BOOLEAN.isType(dataTypeName)) {

            //Set the extra configuration for the grid column related to this table column
            tableColumn.setUiColumnConfiguration("{ xtype: 'checkcolumn', filter: 'boolean' }");

            //Set the extra configuration for the field used in forms and the UI model related to this table column
            tableColumn.setUiFieldConfiguration("{xtype: 'checkbox', inputValue: true, uncheckedValue: false}");

            //Set the extra configuration for the model field used in the UI data models
            tableColumn.setUiModelFieldConfiguration("{type: 'boolean'}");

        } else if (SchemaDataTypes.DATE.isType(dataTypeName)) {

            //Set the extra configuration for the grid column related to this table column
            tableColumn.setUiColumnConfiguration("{xtype: 'datecolumn',format:'m-d-Y', filter: 'date'}");

            //Set the extra configuration for the field used in forms and the UI model related to this table column
            tableColumn.setUiFieldConfiguration("{xtype: 'datefield',anchor: '100%',value: new Date(),format: 'm-d-Y'}");

            //Set the extra configuration for the model field used in the UI data models
            tableColumn.setUiModelFieldConfiguration("{type: 'date', defaultValue: new Date(), dateFormat: 'm-d-Y'}");

        } else if (SchemaDataTypes.STRING.isType(dataTypeName)) {

            //Set the extra configuration for the grid column related to this table column
            tableColumn.setUiColumnConfiguration("{ filter: 'string' }");

            //Set the extra configuration for the field used in forms and the UI model related to this table column
            tableColumn.setUiFieldConfiguration("{xtype: 'textfield'}");

            //Set the extra configuration for the model field used in the UI data models
            tableColumn.setUiModelFieldConfiguration("{type: 'string'}");
        } else if (SchemaDataTypes.FILE.isType(dataTypeName)) {

            //Set the extra configuration for the grid column related to this table column
            tableColumn.setUiColumnConfiguration("{  hidden: true, ignore: true }");

            //Set the extra configuration for the field used in forms and the UI model related to this table column
            tableColumn.setUiFieldConfiguration("{xtype: 'system-file-field'}");

            //Set the extra configuration for the model field used in the UI data models
            tableColumn.setUiModelFieldConfiguration("{type: 'auto'}");
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
            columnRelationship.setParentDisplaySchemaTableColumn(getParentDisplaySchemaTableColumn(columnAttribute));

            columnRelationship.setName(tableColumn.getSchemaTable().getName() + "_" + tableColumn.getName() + " to " + columnRelationship.getParentSchemaTableColumn().getSchemaTable().getName() + "_" + columnRelationship.getParentSchemaTableColumn().getName());

            this.schemaTableColumnRelationshipRepository.save(columnRelationship);

            tableColumn.setSchemaTableColumnRelationship(columnRelationship);

            tableColumn.setUiColumnConfiguration("{ filter: 'entity' }");
            tableColumn.setUiModelFieldConfiguration(null);
            tableColumn.setUiFieldConfiguration(null);

            this.schemaTableColumnRepository.save(tableColumn);
        }
    }

    private SchemaTableColumn getParentDisplaySchemaTableColumn(Attribute<?, ?> columnAttribute) {
        String columnName = ID_TYPE_NAME_LOWERCASE;
        if (ClassUtils.isAssignable(columnAttribute.getJavaType(), NamedEntity.class)) {
            columnName = DEFAULT_DISPLAY_COLUMN_NAME;
        }
        String tableName = ((EntityType) ((SingularAttribute) columnAttribute).getType()).getName();
        SchemaTableColumn stc = this.schemaTableColumnRepository.findBySchemaTableNameAndName(tableName, columnName);
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
    ////////                                          Getter/Setter Methods                                        //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Get the columns attributes for this specific entity
     *
     * @param tableEntityClass
     * @return
     */
    private EntityType<? extends Entity> getEntityType(Class<? extends Entity> tableEntityClass) {
        return getApplicationContext().getBean(EntityManager.class).getMetamodel().entity(tableEntityClass);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Lazy
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
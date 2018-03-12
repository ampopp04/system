/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.schema.table.column;


import com.system.db.entity.named.NamedEntity;
import com.system.db.schema.datatype.SchemaDataType;
import com.system.db.schema.table.SchemaTable;
import com.system.db.schema.table.column.relationship.SchemaTableColumnRelationship;

import javax.persistence.*;

/**
 * The <class>SchemaTableColumn</class> defines database columns.
 * <p>
 * This is a way to track within the database itself which columns have been created
 * and meta-data associated with them.
 *
 * @author Andrew
 */
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"schema_table_id", "name"}),
                @UniqueConstraint(columnNames = {"schema_table_id", "defaultColumnOrder"})
        },
        indexes = {@Index(columnList = "name")}
)
public class SchemaTableColumn extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The table this column is associated with
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "schema_table_id")
    private SchemaTable schemaTable;

    /**
     * The type of data held within this column
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "schema_data_type_id")
    private SchemaDataType schemaDataType;

    /**
     * If this is a foreign key column then this will denote the relationship reference
     */
    @ManyToOne
    @JoinColumn(name = "schema_table_column_relationship_id")
    private SchemaTableColumnRelationship schemaTableColumnRelationship;

    /**
     * This denotes the order that these columns would appear
     * in a logical sense typically in line
     * with what is defined on the database table
     */
    @Column(nullable = false)
    private Integer defaultColumnOrder;

    /**
     * The name that is used for display purposes
     * mostly in the UI.
     */
    private String displayName;

    /**
     * Whether or not this column should be
     * considered a default hidden column
     * throughout the system and
     * mainly in the UI.
     */
    @Column(nullable = false)
    private Boolean displayHidden;

    /**
     * Denotes the extra ui configuration specific to it's grid column
     * configuration within a grid. such as {xtype: 'numbercolumn'}
     * <p>
     * Ex.
     * <p>
     * xtype            Class
     * ---------------------------------------
     * numbercolumn             {Ext.grid.column.Number}
     * {xtype: 'numbercolumn', format:'0.00'}
     * <p>
     * datecolumn       {Ext.grid.column.Date}
     * {xtype: 'datecolumn',format:'Y-m-d'}
     * <p>
     * checkcolumn     {Ext.grid.column.Check}
     * { xtype: 'checkcolumn' }
     * <p>
     * actioncolumn     {Ext.grid.column.Action}
     * Note: In the following example we are
     * <p>
     * essentially adding an edit icon to the grid as a column
     * that is a clickable action.
     * <p>
     * {{
     * xtype:'actioncolumn',
     * width:50,
     * items: [{
     * icon: 'extjs-build/examples/shared/icons/fam/cog_edit.png',  // Use a URL in the icon config
     * tooltip: 'Edit',
     * handler: function(grid, rowIndex, colIndex) {
     * var rec = grid.getStore().getAt(rowIndex);
     * alert("Edit " + rec.get('firstname'));
     * }
     * }]
     * }}
     * <p>
     * booleancolumn {Ext.grid.column.Boolean}
     * {xtype: 'booleancolumn',trueText: 'Yes',falseText: 'No'}
     * <p>
     * templatecolumn {Ext.grid.column.Template }
     * {xtype: 'templatecolumn', tpl: '{department} ({seniority})'}
     * Note: templatecolumn pulls the data as references from the model
     */
    @Column(length = 1000)
    private String uiColumnConfiguration;

    /**
     * Denotes the extra ui configuration specific to it's field
     * configuration within a form or model. such as {xtype: 'numbercolumn'}
     * <p>
     * Ex.
     * <p>
     * xtype            Class
     * ---------------------------------------
     * textfield             {Ext.field.Text}
     * Ex. default
     * numberfield       {Ext.field.Number}
     * {xtype: 'numberfield', minValue: 18, maxValue: 150}
     * <p>
     * textareafield     {Ext.field.TextArea}
     * {xtype: 'textareafield', maxRows: 4}
     * <p>
     * datefield             {Ext.form.field.Date}
     * {xtype: 'datefield',anchor: '100%',value: new Date()}
     * <p>
     * hiddenfield         {Ext.field.Hidden}
     * Note: Simply hides the field on the form
     * <p>
     * {xtype: 'hiddenfield'}
     * <p>
     * filefield                 {Ext.field.File}
     * Note: Allows easy uploading of files!
     * See https://www.w3schools.com/tags/att_input_accept.asp for all
     * supported accept types
     * <p>
     * {xtype: 'filefield',accept: 'image'}
     * <p>
     * checkboxfield    {Ext.field.Checkbox}
     * {xtype: 'checkboxfield', checked: true}
     * <p>
     * selectfield            {Ext.field.Select}
     * Note: Essentially a simplified local combo box.
     * <p>
     * togglefield           {Ext.field.Toggle}
     * Note: A fancy slider version of checkbox.
     * <p>
     * fieldset                 {Ext.form.FieldSet}
     * Note: Pretty container for multiple fields.
     * <p>
     * radiofield            {Ext.field.Radio}
     * Note: Circle checkbox
     */
    @Column(length = 1000)
    private String uiFieldConfiguration;

    /**
     * Denotes the extra ui configuration specific to it's model data field
     * configuration. such as {type: 'string'}
     * <p>
     * Ex.
     * <p>
     * The predefined set of types are:
     * <p>
     * - { Ext.data.field.Field             auto} (Default, implies no conversion)
     * - { Ext.data.field.String           string}
     * - { Ext.data.field.Integer         int}
     * - { Ext.data.field.Number        number}
     * - { Ext.data.field.Boolean       boolean}
     * - { Ext.data.field.Date              date}
     */
    private String uiModelFieldConfiguration;


    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaTableColumn() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                        Advanced  Getter/Setters                                      //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaTableColumn getParentDisplaySchemaTableColumn() {
        return getSchemaTableColumnRelationship() != null ? getSchemaTableColumnRelationship().getParentDisplaySchemaTableColumn() : null;
    }

    public String getDisplayFieldPath() {
        return getSchemaTableColumnRelationship() != null && getSchemaTableColumnRelationship().getParentDisplaySchemaTableColumn() != null ? getName() + "." + getSchemaTableColumnRelationship().getParentDisplaySchemaTableColumn().getName() : getName();
    }

    public String getRelationshipTableName() {
        SchemaTableColumnRelationship schemaTableColumnRelationship = getSchemaTableColumnRelationship();
        if (schemaTableColumnRelationship != null && schemaTableColumnRelationship.getParentSchemaTableColumn() != null) {
            return schemaTableColumnRelationship.getParentSchemaTableColumn().getSchemaTable().getName();
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic  Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaTable getSchemaTable() {
        return schemaTable;
    }

    public void setSchemaTable(SchemaTable schemaTable) {
        this.schemaTable = schemaTable;
    }

    public SchemaDataType getSchemaDataType() {
        return schemaDataType;
    }

    public void setSchemaDataType(SchemaDataType schemaDataType) {
        this.schemaDataType = schemaDataType;
    }

    public SchemaTableColumnRelationship getSchemaTableColumnRelationship() {
        return schemaTableColumnRelationship;
    }

    public void setSchemaTableColumnRelationship(SchemaTableColumnRelationship schemaTableColumnRelationship) {
        this.schemaTableColumnRelationship = schemaTableColumnRelationship;
    }

    public Integer getDefaultColumnOrder() {
        return defaultColumnOrder;
    }

    public void setDefaultColumnOrder(Integer defaultColumnOrder) {
        this.defaultColumnOrder = defaultColumnOrder;
    }

    public String getDisplayName() {
        if (displayName == null) {
            return getName();
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getDisplayHidden() {
        return displayHidden;
    }

    public void setDisplayHidden(Boolean displayHidden) {
        this.displayHidden = displayHidden;
    }

    public String getUiColumnConfiguration() {
        return uiColumnConfiguration;
    }

    public void setUiColumnConfiguration(String uiColumnConfiguration) {
        this.uiColumnConfiguration = uiColumnConfiguration;
    }

    public String getUiFieldConfiguration() {
        return uiFieldConfiguration;
    }

    public void setUiFieldConfiguration(String uiFieldConfiguration) {
        this.uiFieldConfiguration = uiFieldConfiguration;
    }

    public String getUiModelFieldConfiguration() {
        return uiModelFieldConfiguration;
    }

    public void setUiModelFieldConfiguration(String uiModelFieldConfiguration) {
        this.uiModelFieldConfiguration = uiModelFieldConfiguration;
    }
}
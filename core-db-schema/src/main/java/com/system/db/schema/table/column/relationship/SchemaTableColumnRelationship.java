package com.system.db.schema.table.column.relationship;


import com.system.db.entity.base.BaseEntity;
import com.system.db.schema.table.column.SchemaTableColumn;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SchemaTableColumnRelationship</class> defines column relationships.
 * <p>
 * Ex. SchemaColumn has an FK column to SchemaTable therefore we would have a
 * SchemaTableColumnRelationship:
 * schemaTableColumn = SchemaColumn.schemaTable
 * parentSchemaTableColumn = SchemaTable.id
 * <p>
 * This links the SchemaColumn.schemaTable column to the primary key column on the
 * SchemaTable table
 *
 * @author Andrew
 */
public class SchemaTableColumnRelationship extends BaseEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Represents this Schema Table Column
     */
    @ManyToOne
    @JoinColumn(name = "schema_table_column_id")
    private SchemaTableColumn schemaTableColumn;

    /**
     * Represents which column this column is linked to
     */
    @ManyToOne
    @JoinColumn(name = "parent_schema_table_column_id")
    private SchemaTableColumn parentSchemaTableColumn;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaTableColumnRelationship() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////


    public SchemaTableColumn getSchemaTableColumn() {
        return schemaTableColumn;
    }

    public void setSchemaTableColumn(SchemaTableColumn schemaTableColumn) {
        this.schemaTableColumn = schemaTableColumn;
    }

    public SchemaTableColumn getParentSchemaTableColumn() {
        return parentSchemaTableColumn;
    }

    public void setParentSchemaTableColumn(SchemaTableColumn parentSchemaTableColumn) {
        this.parentSchemaTableColumn = parentSchemaTableColumn;
    }
}
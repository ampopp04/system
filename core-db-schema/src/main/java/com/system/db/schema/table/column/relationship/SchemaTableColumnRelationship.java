package com.system.db.schema.table.column.relationship;


import com.system.db.entity.named.NamedEntity;
import com.system.db.schema.table.column.SchemaTableColumn;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SchemaTableColumnRelationship</class> defines column relationships.
 * <p>
 * Ex. SchemaTableColumn has an FK column to SchemaTable therefore we would have a
 * SchemaTableColumnRelationship:
 * schemaTableColumn = SchemaTableColumn.schema_table_id (The SchemaTableColumn representing this column)
 * parentSchemaTableColumn = SchemaTable.id (The SchemaTableColumn representing this column)
 * parentDisplaySchemaTableColumn = SchemaTable.name.
 * <p>
 * * This links the SchemaColumn.schemaTable column to the primary key column on the
 * SchemaTable table while also having a reference to a 'parentDisplaySchemaTableColumn' that
 * can be used to determine which field in the FK relationship table should be used for display in the UI.
 * Since, for a SchemaTableColumn object, we wouldn't want to show the ID of the SchemaTable that it contains but rather
 * some other field from SchemaTable, such as it's name.
 * <p>
 * To Recap:
 * <p>
 * Given a SchemaTableColumn that has as a reference to a SchemaTable
 * <p>
 * We would define a SchemaTableColumnRelationship to show how the
 * SchemaTableColumn that represents schema_table_id on the SchemaTableColumn table
 * is related to that other table, specifically, in this case the SchemeTable.
 * <p>
 * SchemaTableColumn objects in themselves only explain the columns on a specific table but
 * in the instance that one of those columns on a given table references a FK entity, we have no way to know
 * how that SchemaTableColumn for that FK entity actually relates to that specific entity.
 * <p>
 * The SchemaTableColumnRelationship allows us to see the relationship that the FK key, on a given table,
 * relates to another specific table; through the use of defining relationships between SchemaTableColumns.
 *
 * @author Andrew
 */
public class SchemaTableColumnRelationship extends NamedEntity<Integer> {

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

    /**
     * The default display column to use for rendering.  The parentSchemaTableColumn references
     * the parents ID column where this column references the column that should be used for display purposes
     */
    @ManyToOne
    @JoinColumn(name = "parent_display_schema_table_column_id")
    private SchemaTableColumn parentDisplaySchemaTableColumn;

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

    public SchemaTableColumn getParentDisplaySchemaTableColumn() {
        return parentDisplaySchemaTableColumn;
    }

    public void setParentDisplaySchemaTableColumn(SchemaTableColumn parentDisplaySchemaTableColumn) {
        this.parentDisplaySchemaTableColumn = parentDisplaySchemaTableColumn;
    }
}
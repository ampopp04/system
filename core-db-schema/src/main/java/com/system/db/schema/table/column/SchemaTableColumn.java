package com.system.db.schema.table.column;


import com.system.db.entity.named.NamedEntity;
import com.system.db.schema.datatype.SchemaDataType;
import com.system.db.schema.table.SchemaTable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SchemaTableColumn</class> defines database columns.
 * <p>
 * This is a way to track within the database itself which columns have been created
 * and meta-data associated with them.
 *
 * @author Andrew
 */
public class SchemaTableColumn extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The table this column is associated with
     */
    @ManyToOne
    @JoinColumn(name = "schema_table_id")
    private SchemaTable schemaTable;

    /**
     * The type of data held within this column
     */
    @ManyToOne
    @JoinColumn(name = "schema_data_type_id")
    private SchemaDataType schemaDataType;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaTableColumn() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
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
}
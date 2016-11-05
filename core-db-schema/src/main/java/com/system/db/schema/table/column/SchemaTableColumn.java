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
        uniqueConstraints = {@UniqueConstraint(columnNames = {"schema_table_id", "name"})},
        indexes = {@Index(columnList = "name")}
)
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

    /**
     * If this is a foreign key column then this will denote the relationship reference
     */
    @ManyToOne
    @JoinColumn(name = "schema_table_column_relationship_id")
    private SchemaTableColumnRelationship schemaTableColumnRelationship;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaTableColumn() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                        Advanced  Getter/Setters                                      //////////
    //////////////////////////////////////////////////////////////////////

    public String getDisplayFieldPath() {
        return getSchemaTableColumnRelationship() != null && getSchemaTableColumnRelationship().getParentDisplaySchemaTableColumn() != null ? getName() + "." + getSchemaTableColumnRelationship().getParentDisplaySchemaTableColumn().getName() : getName();
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
}
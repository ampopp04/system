package com.system.db.migration.schema.table.column.relationship;

import com.system.db.entity.named.NamedEntity;
import com.system.db.migration.schema.table.column.SystemTableColumn;

import javax.persistence.Entity;

/**
 * The <class>SystemTableColumnRelationship</class> defines column relationships.
 * <p>
 * Ex. SystemColumn has an FK column to SystemTable therefore we would have a
 * SystemTableColumnRelationship:
 * systemTableColumn = SystemColumn.systemTable
 * parentSystemTableColumn = SystemTable.id
 * <p>
 * This links the SystemColumn.systemTable column to the primary key column on the
 * SystemTable table
 *
 * @author Andrew
 */
@Entity
public class SystemTableColumnRelationship extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Represents this System Table Column
     */
    private SystemTableColumn systemTableColumn;
    /**
     * Represents which column this column is linked to
     */
    private SystemTableColumn parentSystemTableColumn;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemTableColumnRelationship() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemTableColumn getSystemTableColumn() {
        return systemTableColumn;
    }

    public void setSystemTableColumn(SystemTableColumn systemTableColumn) {
        this.systemTableColumn = systemTableColumn;
    }

    public SystemTableColumn getParentSystemTableColumn() {
        return parentSystemTableColumn;
    }

    public void setParentSystemTableColumn(SystemTableColumn parentSystemTableColumn) {
        this.parentSystemTableColumn = parentSystemTableColumn;
    }
}
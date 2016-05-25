package com.system.db.migration.schema.table.column;

import com.system.db.entity.named.NamedEntity;
import com.system.db.migration.schema.datatype.SystemDataType;
import com.system.db.migration.schema.table.SystemTable;

import javax.persistence.Entity;

/**
 * The <class>SystemTableColumn</class> defines database columns.
 * <p>
 * This is a way to track within the database itself which columns have been created
 * and meta-data associated with them.
 *
 * @author Andrew
 */
@Entity
public class SystemTableColumn extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The table this column is associated with
     */
    private SystemTable systemTable;

    /**
     * The type of data held within this column
     */
    private SystemDataType systemDataType;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemTableColumn() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////


    public SystemTable getSystemTable() {
        return systemTable;
    }

    public void setSystemTable(SystemTable systemTable) {
        this.systemTable = systemTable;
    }

    public SystemDataType getSystemDataType() {
        return systemDataType;
    }

    public void setSystemDataType(SystemDataType systemDataType) {
        this.systemDataType = systemDataType;
    }
}
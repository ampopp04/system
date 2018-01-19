package com.system.export.definition.type;

import com.system.db.entity.named.NamedEntity;

/**
 * The <class>SystemExportDefinitionType</class> defines
 * a type of export definition relating to details such as what department it is related to or
 * who can modify export information of this type.
 * <p>
 * Export Types Ex.
 * Accounting
 * Project Management
 * Sales
 * Engineering
 * Marketing
 *
 * @author Andrew
 */
public class SystemExportDefinitionType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportDefinitionType() {
    }

    public static SystemExportDefinitionType newInstance(String name, String description) {
        SystemExportDefinitionType systemExportDefinitionType = new SystemExportDefinitionType();
        systemExportDefinitionType.setName(name);
        systemExportDefinitionType.setDescription(description);
        return systemExportDefinitionType;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////


}
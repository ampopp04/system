package com.system.export.destination.type;

import com.system.db.entity.named.NamedEntity;

/**
 * The <class>SystemExportDestinationType</class> defines the
 * types of different destinations that export content can be delivered.
 * <p>
 * Ex. EMAIL, FTP, FAX, UI
 *
 * @author Andrew
 */
public class SystemExportDestinationType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportDestinationType() {
    }

    public static SystemExportDestinationType newInstance(String name, String description) {
        SystemExportDestinationType systemExportDestinationType = new SystemExportDestinationType();
        systemExportDestinationType.setName(name);
        systemExportDestinationType.setDescription(description);
        return systemExportDestinationType;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////


}
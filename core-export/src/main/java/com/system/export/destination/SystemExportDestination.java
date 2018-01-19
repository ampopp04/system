package com.system.export.destination;

import com.system.db.entity.named.NamedEntity;
import com.system.export.destination.type.SystemExportDestinationType;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemExportDestination</class> defines a destination
 * that will specify the information related to delivering content to that destination.
 * <p>
 * For instance, e-mail and faxes will have addresses where those properties are dynamically generated
 * in the UI based on the underlying systemBean and it's required properties.
 *
 * @author Andrew
 */
public class SystemExportDestination extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_export_destination_type_id")
    private SystemExportDestinationType systemExportDestinationType;

    //TODO
    //Perhaps have a SystemBean here which allows the UI user to define dynamic properties related to this destination
    //Since emails and faxes have addresses and such. Right now we are only implement UI Destination which is instant.

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportDestination() {
    }

    public static SystemExportDestination newInstance(String name, String description, SystemExportDestinationType systemExportDestinationType) {
        SystemExportDestination systemExportDestination = new SystemExportDestination();
        systemExportDestination.setName(name);
        systemExportDestination.setDescription(description);
        systemExportDestination.setSystemExportDestinationType(systemExportDestinationType);
        return systemExportDestination;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportDestinationType getSystemExportDestinationType() {
        return systemExportDestinationType;
    }

    public void setSystemExportDestinationType(SystemExportDestinationType systemExportDestinationType) {
        this.systemExportDestinationType = systemExportDestinationType;
    }
}
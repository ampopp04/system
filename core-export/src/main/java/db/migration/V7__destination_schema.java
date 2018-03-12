/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package db.migration;


import com.system.bean.SystemBean;
import com.system.bean.type.SystemBeanType;
import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.export.destination.SystemExportDestination;
import com.system.export.destination.type.SystemExportDestinationType;
import com.system.util.collection.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V7__destination_schema</class> defines the
 * export destination schema that specifies
 * export content delivery destination information.  These entities are meta-data
 * holders that allow creating the delivery information required to arrive at a specific destination.
 * <p>
 * Ex Types: EMAIL, FTP, FAX, UI
 * <p>
 * UI type would be instant.
 * <p>
 * EMAIL, FTP, and FAX would have address information along with other type specific information.
 * <p>
 * A {@link SystemExportDestination} contains a {@link SystemBean} which allows properties to be dynamically
 * generated in the UI based on it's {@link SystemExportDestinationType} which contains a {@link SystemBeanType} which can
 * specify a set of dynamically related properties.
 *
 * @author Andrew
 */
public class V7__destination_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<SystemExportDestinationType> systemExportDestinationTypeRepository;

    @Autowired
    private NamedEntityRepository<SystemExportDestination> systemExportDestinationRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return CollectionUtils.asList(
                SystemExportDestinationType.class, SystemExportDestination.class
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getSystemExportDestinationTypeRepository().saveAll(getSystemExportDestinationTypeList());
        getSystemExportDestinationRepository().saveAll(getSystemExportDestinationList());
    }

    public List<SystemExportDestinationType> getSystemExportDestinationTypeList() {
        return asList(
                SystemExportDestinationType.newInstance("UI", "A UI destination specifies that we are immediately returning the generated report back to the UI user."),
                SystemExportDestinationType.newInstance("EMAIL", "An email destination where a report can be sent."),
                SystemExportDestinationType.newInstance("FTP", "An ftp destination where a report can be sent."),
                SystemExportDestinationType.newInstance("FAX", "A fax destination where a report can be sent.")
        );
    }

    public List<SystemExportDestination> getSystemExportDestinationList() {
        return asList(
                SystemExportDestination.newInstance("User Interface", "Deliver content back to the user on the website. Used for downloading reports from the website.", getSystemExportDestinationTypeRepository().findByName("UI"))
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////


    public NamedEntityRepository<SystemExportDestinationType> getSystemExportDestinationTypeRepository() {
        return systemExportDestinationTypeRepository;
    }

    public void setSystemExportDestinationTypeRepository(NamedEntityRepository<SystemExportDestinationType> systemExportDestinationTypeRepository) {
        this.systemExportDestinationTypeRepository = systemExportDestinationTypeRepository;
    }

    public NamedEntityRepository<SystemExportDestination> getSystemExportDestinationRepository() {
        return systemExportDestinationRepository;
    }

    public void setSystemExportDestinationRepository(NamedEntityRepository<SystemExportDestination> systemExportDestinationRepository) {
        this.systemExportDestinationRepository = systemExportDestinationRepository;
    }
}
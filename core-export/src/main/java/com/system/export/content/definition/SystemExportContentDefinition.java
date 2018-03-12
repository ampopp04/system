/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.content.definition;

import com.system.db.entity.named.NamedEntity;
import com.system.export.SystemExport;
import com.system.export.generator.SystemExportGenerator;
import com.system.export.template.SystemExportTemplate;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemExportContentDefinition</class> defines a connection
 * between an {@link SystemExport}, a {@link SystemExportTemplate}, and a {@link SystemExportGenerator}
 * <p>
 * This is a definition linking the pieces needed in generating an export with populated data.
 * <p>
 * The template itself, the export meta-data to assist in generating the export and the generator itself that
 * will generate an output based on it's own generation strategy.
 *
 * @author Andrew
 */
public class SystemExportContentDefinition extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_export_id")
    private SystemExport systemExport;

    @ManyToOne
    @JoinColumn(name = "system_export_generator_id")
    private SystemExportGenerator systemExportGenerator;

    @ManyToOne
    @JoinColumn(name = "system_export_template_id")
    private SystemExportTemplate systemExportTemplate;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportContentDefinition() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExport getSystemExport() {
        return systemExport;
    }

    public void setSystemExport(SystemExport systemExport) {
        this.systemExport = systemExport;
    }

    public SystemExportGenerator getSystemExportGenerator() {
        return systemExportGenerator;
    }

    public void setSystemExportGenerator(SystemExportGenerator systemExportGenerator) {
        this.systemExportGenerator = systemExportGenerator;
    }

    public SystemExportTemplate getSystemExportTemplate() {
        return systemExportTemplate;
    }

    public void setSystemExportTemplate(SystemExportTemplate systemExportTemplate) {
        this.systemExportTemplate = systemExportTemplate;
    }

}
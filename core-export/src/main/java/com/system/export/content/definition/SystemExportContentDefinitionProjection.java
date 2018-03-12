/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.content.definition;

import com.system.export.SystemExportProjection;
import com.system.export.generator.SystemExportGenerator;
import com.system.export.template.SystemExportTemplate;
import org.springframework.data.rest.core.config.Projection;

/**
 * The <class>SystemExportContentDefinitionProjection</class> defines a projection
 * for all the fields of a SystemExportContentDefinition
 *
 * @author Andrew
 */
@Projection(name = "system-export-content-definition-all", types = SystemExportContentDefinition.class)
public interface SystemExportContentDefinitionProjection {

    public SystemExportProjection getSystemExport();

    public SystemExportGenerator getSystemExportGenerator();

    public SystemExportTemplate getSystemExportTemplate();

    public String getName();

    public String getDescription();

    public Integer getId();
}
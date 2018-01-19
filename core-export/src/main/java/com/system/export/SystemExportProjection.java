package com.system.export;

import com.system.export.definition.SystemExportDefinition;
import com.system.export.file.type.SystemExportFileType;
import org.springframework.data.rest.core.config.Projection;

/**
 * The <class>SystemExportProjection</class> defines a projection
 * for all the fields of a SystemExport
 *
 * @author Andrew
 */
@Projection(name = "system-export-all", types = SystemExport.class)
public interface SystemExportProjection {

    public SystemExportDefinition getSystemExportDefinition();

    public SystemExportFileType getSystemExportFileType();

    public String getName();

    public String getDescription();

    public Integer getId();
}
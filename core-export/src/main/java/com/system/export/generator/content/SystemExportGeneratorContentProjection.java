package com.system.export.generator.content;

import com.system.export.content.definition.SystemExportContentDefinitionProjection;
import com.system.export.file.type.SystemExportFileType;
import org.springframework.data.rest.core.config.Projection;

/**
 * The <class>SystemExportGeneratorContentProjection</class> defines a projection
 * for all the fields of a SystemExportGeneratorContent
 *
 * @author Andrew
 */
@Projection(name = "system-export-generator-content-all", types = SystemExportGeneratorContent.class)
public interface SystemExportGeneratorContentProjection {

    public SystemExportContentDefinitionProjection getSystemExportContentDefinition();

    public SystemExportFileType getSystemExportFileType();

    public byte[] getExportContent();

    public String getName();

    public String getDescription();

    public Integer getId();
}
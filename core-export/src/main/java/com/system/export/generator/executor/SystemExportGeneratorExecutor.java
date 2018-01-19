package com.system.export.generator.executor;

import com.system.export.content.definition.SystemExportContentDefinition;
import com.system.export.generator.content.SystemExportGeneratorContent;

/**
 * The <class>SystemExportGeneratorExecutor</class> defines
 * an interface for generating exports via various generator executors.
 *
 * @author Andrew
 */
public interface SystemExportGeneratorExecutor {

    /**
     * Generate the export based on this executors implementation
     */
    public SystemExportGeneratorContent generate(SystemExportContentDefinition systemExportContentDefinition, Object dataObject);

}
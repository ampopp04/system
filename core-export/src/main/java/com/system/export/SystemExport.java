package com.system.export;

import com.system.db.entity.named.NamedEntity;
import com.system.export.definition.SystemExportDefinition;
import com.system.export.file.type.SystemExportFileType;
import com.system.export.template.SystemExportTemplate;
import com.system.export.variable.SystemExportVariableMapping;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemExport</class> is essentially a shell for
 * meta-data surrounding a relationship to a template.
 * <p>
 * There is most likely some {@link SystemExportTemplate} related
 * to this SystemExport but this not only defines meta-data surrounding the
 * specifics of a possible {@link SystemExportTemplate} but also allows for
 * other tables to associate meta-data towards this SystemExport such as {@link SystemExportVariableMapping}
 *
 * @author Andrew
 */
public class SystemExport extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "system_export_definition_id")
    private SystemExportDefinition systemExportDefinition;

    @ManyToOne
    @JoinColumn(name = "system_export_file_type_id")
    private SystemExportFileType systemExportFileType;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExport() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportDefinition getSystemExportDefinition() {
        return systemExportDefinition;
    }

    public void setSystemExportDefinition(SystemExportDefinition systemExportDefinition) {
        this.systemExportDefinition = systemExportDefinition;
    }

    public SystemExportFileType getSystemExportFileType() {
        return systemExportFileType;
    }

    public void setSystemExportFileType(SystemExportFileType systemExportFileType) {
        this.systemExportFileType = systemExportFileType;
    }

}
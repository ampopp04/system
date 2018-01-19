package com.system.export.template;

import com.system.db.entity.named.NamedEntity;
import com.system.export.file.type.SystemExportFileType;
import com.system.export.template.type.SystemExportTemplateType;

import javax.persistence.Basic;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

/**
 * The <class>SystemExportTemplate</class> defines an
 * instance of a template file used in exports
 *
 * @author Andrew
 */
public class SystemExportTemplate extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The type of template this is, Report File, Query, Calculation, etc
     */
    @ManyToOne
    @JoinColumn(name = "system_export_template_type_id")
    private SystemExportTemplateType systemExportTemplateType;

    /**
     * The extension of this file content, pdf, xls, docx, txt, etc
     */
    @ManyToOne
    @JoinColumn(name = "system_export_file_type_id")
    private SystemExportFileType systemExportFileType;

    /**
     * The actual raw template
     */
    @Lob
    @Basic(fetch = LAZY)
    private byte[] templateContent;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTemplate() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportTemplateType getSystemExportTemplateType() {
        return systemExportTemplateType;
    }

    public void setSystemExportTemplateType(SystemExportTemplateType systemExportTemplateType) {
        this.systemExportTemplateType = systemExportTemplateType;
    }

    public SystemExportFileType getSystemExportFileType() {
        return systemExportFileType;
    }

    public void setSystemExportFileType(SystemExportFileType systemExportFileType) {
        this.systemExportFileType = systemExportFileType;
    }

    public byte[] getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(byte[] templateContent) {
        this.templateContent = templateContent;
    }
}
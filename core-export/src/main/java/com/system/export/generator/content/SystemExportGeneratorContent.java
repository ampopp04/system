package com.system.export.generator.content;

import com.system.db.entity.named.NamedEntity;
import com.system.export.content.definition.SystemExportContentDefinition;
import com.system.export.file.type.SystemExportFileType;
import com.system.export.generator.SystemExportGenerator;

import javax.persistence.Basic;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

/**
 * The <class>SystemExportGeneratorContent</class> defines a record
 * of the content generated by a {@link SystemExportGenerator}
 * <p>
 * The export generator will run and produce an output content result that is usually a template
 * that is applied against a set of data and transformations. This result, the final complete report, is
 * then saved within this object to the database.
 * <p>
 * This saves the generated report for further use and will be used to return the export back to the user or
 * send it off to another remote destination (e-mail, fax, etc) while maintaining a copy of it for historical purposes.
 *
 * @author Andrew
 */
public class SystemExportGeneratorContent extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The export content definition that defines how this content was actually generated
     * This includes the export meta-data regarding the specifics of export generation details
     * along with a template that was used to actually generate the export
     * along with specifying the actual generator that was used to produce the export content
     */
    @ManyToOne
    @JoinColumn(name = "system_export_content_definition_id")
    private SystemExportContentDefinition systemExportContentDefinition;

    /**
     * The extension of this file content, pdf, xls, docx, txt, etc
     */
    @ManyToOne
    @JoinColumn(name = "system_export_file_type_id")
    private SystemExportFileType systemExportFileType;

    /**
     * The actual finished export fully populated and generated
     */
    @Lob
    @Basic(fetch = LAZY)
    private byte[] exportContent;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportGeneratorContent() {
    }

    public static SystemExportGeneratorContent newInstance(String name, String description, SystemExportContentDefinition systemExportContentDefinition, byte[] exportContent, SystemExportFileType systemExportFileType) {
        SystemExportGeneratorContent systemExportGeneratorContent = new SystemExportGeneratorContent();
        systemExportGeneratorContent.setName(name);
        systemExportGeneratorContent.setDescription(description);
        systemExportGeneratorContent.setSystemExportContentDefinition(systemExportContentDefinition);
        systemExportGeneratorContent.setExportContent(exportContent);
        systemExportGeneratorContent.setSystemExportFileType(systemExportFileType);
        return systemExportGeneratorContent;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemExportContentDefinition getSystemExportContentDefinition() {
        return systemExportContentDefinition;
    }

    public void setSystemExportContentDefinition(SystemExportContentDefinition systemExportContentDefinition) {
        this.systemExportContentDefinition = systemExportContentDefinition;
    }

    public SystemExportFileType getSystemExportFileType() {
        return systemExportFileType;
    }

    public void setSystemExportFileType(SystemExportFileType systemExportFileType) {
        this.systemExportFileType = systemExportFileType;
    }

    public byte[] getExportContent() {
        return exportContent;
    }

    public void setExportContent(byte[] exportContent) {
        this.exportContent = exportContent;
    }
}
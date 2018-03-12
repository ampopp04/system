/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package db.migration;


import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.export.file.type.SystemExportFileType;
import com.system.export.template.SystemExportTemplate;
import com.system.export.template.type.SystemExportTemplateType;
import com.system.util.collection.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V4__initial_schema</class> defines the initial
 * database migration schema for export templates
 * <p>
 * These are mainly the raw template content holders for template report files,
 * queries, or template calculations.
 *
 * @author Andrew
 * @see SystemExportTemplateType
 * @see SystemExportTemplate
 */
public class V4__template_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<SystemExportFileType> systemExportFileTypeRepository;

    @Autowired
    private NamedEntityRepository<SystemExportTemplateType> systemExportTemplateTypeRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return CollectionUtils.asList(
                SystemExportFileType.class,
                SystemExportTemplateType.class, SystemExportTemplate.class
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getSystemExportFileTypeRepository().saveAll(getSystemExportFileTypeList());
        getSystemExportTemplateTypeRepository().saveAll(getSystemExportTemplateTypeList());
    }


    public List<SystemExportFileType> getSystemExportFileTypeList() {
        return asList(
                SystemExportFileType.newInstance("xls", "Excel spreadsheet", "application/vnd.ms-excel"),
                SystemExportFileType.newInstance("doc", "Microsoft Word", "application/msword"),
                SystemExportFileType.newInstance("docx", "Microsoft Word", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
                SystemExportFileType.newInstance("xlsx", "Excel spreadsheet", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
                SystemExportFileType.newInstance("html", "Web page source text", "text/html"),
                SystemExportFileType.newInstance("pdf", "Portable Document Format", "application/pdf"),
                SystemExportFileType.newInstance("csv", "Comma Separated Values", "text/csv")
        );
    }

    public List<SystemExportTemplateType> getSystemExportTemplateTypeList() {
        return asList(
                SystemExportTemplateType.newInstance("Report", "A report that is generated from a report template that is applied to a specific entity within the system."),
                SystemExportTemplateType.newInstance("Query", "A query that executes a directly against the system to obtain diverse sets of data and applies it to a template or simply exports the results directly.")
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<SystemExportFileType> getSystemExportFileTypeRepository() {
        return systemExportFileTypeRepository;
    }

    public void setSystemExportFileTypeRepository(NamedEntityRepository<SystemExportFileType> systemExportFileTypeRepository) {
        this.systemExportFileTypeRepository = systemExportFileTypeRepository;
    }

    public NamedEntityRepository<SystemExportTemplateType> getSystemExportTemplateTypeRepository() {
        return systemExportTemplateTypeRepository;
    }

    public void setSystemExportTemplateTypeRepository(NamedEntityRepository<SystemExportTemplateType> systemExportTemplateTypeRepository) {
        this.systemExportTemplateTypeRepository = systemExportTemplateTypeRepository;
    }
}
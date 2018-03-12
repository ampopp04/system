/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.export.generator.executor.report;

import com.haulmont.yarg.formatters.factory.DefaultFormatterFactory;
import com.haulmont.yarg.formatters.impl.doc.connector.OfficeIntegration;
import com.haulmont.yarg.loaders.factory.DefaultLoaderFactory;
import com.haulmont.yarg.reporting.ReportOutputDocument;
import com.haulmont.yarg.reporting.Reporting;
import com.haulmont.yarg.reporting.RunParams;
import com.haulmont.yarg.structure.*;
import com.haulmont.yarg.structure.impl.ReportBandImpl;
import com.haulmont.yarg.structure.impl.ReportBuilder;
import com.haulmont.yarg.structure.impl.ReportQueryImpl;
import com.haulmont.yarg.structure.impl.ReportTemplateBuilder;
import com.haulmont.yarg.util.groovy.DefaultScriptingImpl;
import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.export.SystemExport;
import com.system.export.content.definition.SystemExportContentDefinition;
import com.system.export.file.type.SystemExportFileType;
import com.system.export.generator.content.SystemExportGeneratorContent;
import com.system.export.generator.executor.SystemExportGeneratorExecutor;
import com.system.logging.exception.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.util.FieldUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static com.haulmont.yarg.loaders.factory.DefaultLoaderFactory.GROOVY_DATA_LOADER;
import static com.haulmont.yarg.structure.BandData.ROOT_BAND_NAME;
import static com.system.util.collection.CollectionUtils.*;
import static com.system.ws.entity.upload.util.EntityPropertyUtils.getEntityBeanPropertyNameValueMap;

/**
 * The <class>SystemExportGeneratorExecutorReport</class> generates
 * reports from templates for a given export.
 *
 * @author Andrew
 */
public class SystemExportGeneratorExecutorReport implements SystemExportGeneratorExecutor {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NamedEntityRepository<SystemExportGeneratorContent> systemExportGeneratorContentRepository;

    @Autowired
    private NamedEntityRepository<SystemExportFileType> systemExportFileTypeRepository;

    @Value("${cuba.reporting.openoffice.path}")
    private String openOfficePath;

    @Value("${cuba.reporting.openoffice.ports}")
    private int[] openOfficePorts;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Interface Entry Point                                          //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public SystemExportGeneratorContent generate(SystemExportContentDefinition systemExportContentDefinition, Object dataObject) {
        SystemExport systemExport = systemExportContentDefinition.getSystemExport();

        ReportOutputDocument output = do_generate_report(systemExport.getName(), systemExport.getSystemExportFileType().getName(), systemExportContentDefinition.getSystemExportTemplate().getTemplateContent(), dataObject);

        ReportOutputType fileType = SystemExportFileType.SystemExportFileTypes.getOutputTypeById(output.getReportOutputType().getId());
        SystemExportFileType exportFileType = getSystemExportFileTypeRepository().findByName(fileType.getId());

        return getSystemExportGeneratorContentRepository().save(
                SystemExportGeneratorContent.newInstance(
                        output.getDocumentName(),
                        output.getReport().getName(),
                        systemExportContentDefinition,
                        output.getContent(),
                        exportFileType
                )
        );
    }

    public ReportOutputDocument do_generate_report(String exportName, String exportFileTypeName, byte[] template, Object dataObject) {
        ReportBuilder reportBuilder = loadTemplate(exportName, exportFileTypeName, template);
        Report report = loadReportData(reportBuilder, dataObject);
        return loadReport(report, dataObject);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                       Generation Support Methods                                //////////
    //////////////////////////////////////////////////////////////////////

    private ReportBuilder loadTemplate(String documentName, String fileType, byte[] templateContents) {
        ReportBuilder reportBuilder = new ReportBuilder();

        try {
            ReportOutputType reportOutputType = ReportOutputType.getOutputTypeById(SystemExportFileType.SystemExportFileTypes.getOutputTypeById(fileType).getId());

            ReportTemplateBuilder reportTemplateBuilder = new ReportTemplateBuilder()
                    .documentName(documentName + "." + reportOutputType.getId())
                    .documentContent(templateContents)
                    .documentPath(documentName + "." + fileType)
                    .outputType(reportOutputType);

            reportBuilder.template(reportTemplateBuilder.build());
            reportBuilder.name(documentName);

        } catch (Exception e) {
            ExceptionUtils.throwSystemException("Could not prepare report template builder.", e);
        }

        return reportBuilder;
    }

    private Report loadReportData(ReportBuilder reportBuilder, Object dataObject) {
        ReportBandImpl rootBand = getNewParentBand(ROOT_BAND_NAME, null);
        getAndSetChildReportBandData(rootBand, getTemplateDataModel(dataObject));

        setReportTemplateParameterVariableMappings(reportBuilder);

        Report report = reportBuilder.build();

        FieldUtils.setProtectedFieldValue("rootBand", report, rootBand);

        return report;
    }

    private ReportQuery getReportQueryData(String name, Map<String, Object> queryParamData) {
        return new ReportQueryImpl(name, "", GROOVY_DATA_LOADER, null, queryParamData);
    }

    private void getAndSetChildReportBandData(ReportBandImpl parentBand, Map<String, Object> queryParamData) {
        Collection<ReportBand> childReportBandList = newList();
        Map<String, Object> directBandDataMap = newMap();

        iterate(iterable(queryParamData), queryParamDataKey -> {
            Object queryParamDataValue = queryParamData.get(queryParamDataKey);

            if (queryParamDataValue instanceof Map) {
                ReportBandImpl childReportBandImpl = getNewParentBand(queryParamDataKey, parentBand);
                getAndSetChildReportBandData(childReportBandImpl, (Map) queryParamDataValue);
                childReportBandList.add(childReportBandImpl);
            } else {
                //We found a simple value that should be data for our current parent band
                directBandDataMap.put(queryParamDataKey, queryParamDataValue);
            }
        });

        //All recursion is complete and all direct band fields are determined for this iteration of the recursion for our current parent band
        //This means that we can force set the reportQueryData and the child report band list
        FieldUtils.setProtectedFieldValue("reportQueries", parentBand, asList(getReportQueryData(parentBand.getName(), directBandDataMap)));
        FieldUtils.setProtectedFieldValue("childrenBandDefinitions", parentBand, childReportBandList);
    }

    private ReportBandImpl getNewParentBand(String name, ReportBandImpl parentBand) {
        return new ReportBandImpl(
                name,
                parentBand,
                newList(),
                newList(),
                BandOrientation.HORIZONTAL
        );
    }

    private ReportOutputDocument loadReport(Report report, Object dataObject) {
        ReportOutputDocument reportOutputDocument = null;

        try {

            DefaultFormatterFactory reportFormatterFactory = new SystemFormatterFactory(dataObject);
            reportFormatterFactory.setOfficeIntegration(new OfficeIntegration(openOfficePath, IntStream.of(openOfficePorts).boxed().toArray(Integer[]::new)));

            Reporting reporting = new Reporting();

            reporting.setFormatterFactory(reportFormatterFactory);
            reporting.setLoaderFactory(new DefaultLoaderFactory().setGroovyDataLoader(
                    new PropertyDataLoader(
                            new DefaultScriptingImpl()
                    )
            ));

            RunParams runParams = new RunParams(report).params(report.getRootBand().getReportQueries().get(0).getAdditionalParams());
            runParams.output(report.getReportTemplates().get(ReportTemplate.DEFAULT_TEMPLATE_CODE).getOutputType());

            reportOutputDocument = reporting.runReport(runParams);

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionUtils.throwSystemException("Could not read data for export generation.", e);
        }

        return reportOutputDocument;
    }

    private Map<String, Object> getTemplateDataModel(Object dataObject) {
        Map<String, Object> beanPropertyValueMap = new HashMap<>();

        createTemplateDataModel(beanPropertyValueMap, dataObject);
        logger.trace("BeanPropertyValueMapJson data : [" + beanPropertyValueMap + "]");

        return beanPropertyValueMap;
    }

    private void createTemplateDataModel(Map<String, Object> root, Object rootEntity) {
        Map<String, Object> propertyNameValueMap = getEntityBeanPropertyNameValueMap((BaseEntity) rootEntity);
        root.putAll(propertyNameValueMap);
    }

    //TODO
    private void setReportTemplateParameterVariableMappings(ReportBuilder reportBuilder) {
        //reportBuilder.parameter(new ReportParameterImpl());
    }


    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<SystemExportGeneratorContent> getSystemExportGeneratorContentRepository() {
        return systemExportGeneratorContentRepository;
    }

    public void setSystemExportGeneratorContentRepository(NamedEntityRepository<SystemExportGeneratorContent> systemExportGeneratorContentRepository) {
        this.systemExportGeneratorContentRepository = systemExportGeneratorContentRepository;
    }

    public NamedEntityRepository<SystemExportFileType> getSystemExportFileTypeRepository() {
        return systemExportFileTypeRepository;
    }

    public void setSystemExportFileTypeRepository(NamedEntityRepository<SystemExportFileType> systemExportFileTypeRepository) {
        this.systemExportFileTypeRepository = systemExportFileTypeRepository;
    }

}
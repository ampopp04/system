/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package db.migration;


import com.system.bean.SystemBean;
import com.system.bean.definition.SystemBeanDefinition;
import com.system.bean.definition.type.SystemBeanDefinitionType;
import com.system.bean.type.SystemBeanType;
import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.export.generator.SystemExportGenerator;
import com.system.export.generator.type.SystemExportGeneratorType;
import com.system.util.collection.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The <class>V6__generator_schema</class> defines the initial
 * schema for export generators that generate export content based on templates
 * and export definition meta-data along with provided data.
 *
 * @author Andrew
 */
public class V6__generator_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<SystemBeanDefinitionType> systemBeanDefinitionTypeRepository;

    @Autowired
    private NamedEntityRepository<SystemBeanDefinition> systemBeanDefinitionRepository;

    @Autowired
    private NamedEntityRepository<SystemBeanType> systemBeanTypeRepository;

    @Autowired
    private NamedEntityRepository<SystemBean> systemBeanRepository;

    @Autowired
    private NamedEntityRepository<SystemExportGeneratorType> systemExportGeneratorTypeRepository;

    @Autowired
    private NamedEntityRepository<SystemExportGenerator> systemExportGeneratorRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return CollectionUtils.asList(
                SystemExportGeneratorType.class, SystemExportGenerator.class
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        SystemBeanDefinition systemExportGeneratorExecutor = createAndSaveSystemBeanDefinition();
        SystemBeanType systemExportGeneratorExecutorReport = createAndSaveSystemBeanType(systemExportGeneratorExecutor);
        SystemBean standardReportGenerator = createAndSaveSystemBean(systemExportGeneratorExecutorReport);

        SystemExportGeneratorType reportSystemExportGeneratorType = createAndSaveReportGeneratorType(systemExportGeneratorExecutorReport);

        createAndSaveStandardReportGenerator(reportSystemExportGeneratorType, standardReportGenerator);
    }

    private SystemExportGeneratorType createAndSaveReportGeneratorType(SystemBeanType systemExportGeneratorExecutorReport) {
        SystemExportGeneratorType reportSystemExportGeneratorType = new SystemExportGeneratorType();
        reportSystemExportGeneratorType.setName("Report Generator");
        reportSystemExportGeneratorType.setDescription("Produces a report based on a template, export meta-data, and entity data.");
        reportSystemExportGeneratorType.setSystemBeanType(systemExportGeneratorExecutorReport);

        return getSystemExportGeneratorTypeRepository().save(reportSystemExportGeneratorType);
    }

    private SystemExportGenerator createAndSaveStandardReportGenerator(SystemExportGeneratorType reportSystemExportGeneratorType, SystemBean standardReportGenerator) {
        SystemExportGenerator reportSystemExportGenerator = new SystemExportGenerator();
        reportSystemExportGenerator.setName("Standard Report Generator");
        reportSystemExportGenerator.setDescription("Generates a report with file type as defined on the associated export data using the associated template.");
        reportSystemExportGenerator.setSystemExportGeneratorType(reportSystemExportGeneratorType);
        reportSystemExportGenerator.setSystemBean(standardReportGenerator);

        return getSystemExportGeneratorRepository().save(reportSystemExportGenerator);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Bean Creation                                                 //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Create new SystemBeanDefinition Interface System Export Generator Executor
     */
    private SystemBeanDefinition createAndSaveSystemBeanDefinition() {
        SystemBeanDefinition systemBeanDefinition = new SystemBeanDefinition();
        systemBeanDefinition.setName("System Export Generator Executor");
        systemBeanDefinition.setSystemBeanDefinitionType(getSystemBeanDefinitionTypeRepository().findByName("Interface"));
        systemBeanDefinition.setDescription("An interface for generating exports via various generator executors.");
        systemBeanDefinition.setClassName("com.system.export.generator.executor.SystemExportGeneratorExecutor");
        return getSystemBeanDefinitionRepository().save(systemBeanDefinition);
    }

    /**
     * Create SystemBeanType - System Export Generator Executor Report
     */
    private SystemBeanType createAndSaveSystemBeanType(SystemBeanDefinition systemBeanDefinition) {
        SystemBeanType systemBeanType = new SystemBeanType();
        systemBeanType.setName("Report Generator Executor");
        systemBeanType.setDescription("Concrete class implementation that executes the generation of reports from templates for a given export.");
        systemBeanType.setClassName("com.system.export.generator.executor.report.SystemExportGeneratorExecutorReport");
        systemBeanType.setSystemBeanDefinition(systemBeanDefinition);
        return getSystemBeanTypeRepository().save(systemBeanType);
    }

    /**
     * Create SystemBean Operation Executor
     */
    private SystemBean createAndSaveSystemBean(SystemBeanType systemBeanType) {
        SystemBean systemBean = new SystemBean();
        systemBean.setName("Standard Report Generator");
        systemBean.setDescription("Generates a report in the same format as the template based on export meta-data, a template, and entity data.");
        systemBean.setSystemBeanType(systemBeanType);
        return getSystemBeanRepository().save(systemBean);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<SystemBeanDefinitionType> getSystemBeanDefinitionTypeRepository() {
        return systemBeanDefinitionTypeRepository;
    }

    public void setSystemBeanDefinitionTypeRepository(NamedEntityRepository<SystemBeanDefinitionType> systemBeanDefinitionTypeRepository) {
        this.systemBeanDefinitionTypeRepository = systemBeanDefinitionTypeRepository;
    }

    public NamedEntityRepository<SystemBeanDefinition> getSystemBeanDefinitionRepository() {
        return systemBeanDefinitionRepository;
    }

    public void setSystemBeanDefinitionRepository(NamedEntityRepository<SystemBeanDefinition> systemBeanDefinitionRepository) {
        this.systemBeanDefinitionRepository = systemBeanDefinitionRepository;
    }

    public NamedEntityRepository<SystemBeanType> getSystemBeanTypeRepository() {
        return systemBeanTypeRepository;
    }

    public void setSystemBeanTypeRepository(NamedEntityRepository<SystemBeanType> systemBeanTypeRepository) {
        this.systemBeanTypeRepository = systemBeanTypeRepository;
    }

    public NamedEntityRepository<SystemBean> getSystemBeanRepository() {
        return systemBeanRepository;
    }

    public void setSystemBeanRepository(NamedEntityRepository<SystemBean> systemBeanRepository) {
        this.systemBeanRepository = systemBeanRepository;
    }

    public NamedEntityRepository<SystemExportGeneratorType> getSystemExportGeneratorTypeRepository() {
        return systemExportGeneratorTypeRepository;
    }

    public void setSystemExportGeneratorTypeRepository(NamedEntityRepository<SystemExportGeneratorType> systemExportGeneratorTypeRepository) {
        this.systemExportGeneratorTypeRepository = systemExportGeneratorTypeRepository;
    }

    public NamedEntityRepository<SystemExportGenerator> getSystemExportGeneratorRepository() {
        return systemExportGeneratorRepository;
    }

    public void setSystemExportGeneratorRepository(NamedEntityRepository<SystemExportGenerator> systemExportGeneratorRepository) {
        this.systemExportGeneratorRepository = systemExportGeneratorRepository;
    }
}
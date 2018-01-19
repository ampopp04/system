package com.system.export.generator;

import com.google.common.io.ByteStreams;
import com.haulmont.yarg.reporting.ReportOutputDocument;
import com.system.bean.SystemBean;
import com.system.bean.service.SystemBeanService;
import com.system.db.migration.base.BaseIntegrationTest;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.export.generator.executor.report.SystemExportGeneratorExecutorReport;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class SystemExportGeneratorExecutorReportTest extends BaseIntegrationTest {

    @Autowired
    private SystemBeanService systemBeanService;

    @Value("classpath:SystemBeanCoverReport.docx")
    private Resource coverPageReportTemplate;

    @Autowired
    private NamedEntityRepository<SystemBean> systemBeanRepository;

    @Test
    public void generateSimpleReport() throws IOException {
        SystemBean standardReportGeneratorBean = systemBeanRepository.findByName("Standard Report Generator");
        SystemExportGeneratorExecutorReport generatorExecutorReport = (SystemExportGeneratorExecutorReport) systemBeanService.getSystemBeanInstance(standardReportGeneratorBean);

        String fileName = "SystemBeanCoverReport";
        String fileExtension = "docx";

        ReportOutputDocument generatedReport = generatorExecutorReport.do_generate_report(fileName, fileExtension, ByteStreams.toByteArray(coverPageReportTemplate.getInputStream()), standardReportGeneratorBean);

        //Confirm we converted to content
        assertNotNull(generatedReport.getContent());

        //Generate the actual file and check that it was created
        String filePathName = fileName + "-output-report." + generatedReport.getReportOutputType().getId();
        File exportReportFile = new File(filePathName);
        FileUtils.writeByteArrayToFile(exportReportFile, generatedReport.getContent());

        assertTrue(Files.exists(Paths.get(filePathName)));
    }

}
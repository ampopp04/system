package com.system.export.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:reporting.properties"})
public class SystemExportConfig {
}

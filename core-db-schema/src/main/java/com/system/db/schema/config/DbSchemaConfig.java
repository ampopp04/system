package com.system.db.schema.config;

import com.system.db.schema.table.processor.SchemaTableProcessor;
import org.flywaydb.core.api.callback.FlywayCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The <class>DbSchemaConfig</class> defines the base
 * Spring configuration for the db schema project
 *
 * @author Andrew
 * @see Configuration
 */
@Configuration
public class DbSchemaConfig {

    /**
     * Register a {@link FlywayCallback} that will listen for
     * new Table creations.
     *
     * @return
     * @see SchemaTableProcessor
     */
    @Bean
    public SchemaTableProcessor callback() {
        return new SchemaTableProcessor();
    }
}
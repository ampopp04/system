package com.system.db.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.system")
public class DbMigrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbMigrationApplication.class, args);
    }

}
package com.system.inversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.system")
public class InversionContainer {

    public static void startInversion(String[] args) {
        SpringApplication.run(InversionContainer.class, args);
    }

    public static void main(String[] args) {
        InversionContainer.startInversion(args);
    }

}

package com.system.inversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The <class>InversionContainer</class> is the entry point for
 * starting the inversion control process
 *
 * @author Andrew
 * @see SpringApplication
 */
@SpringBootApplication(scanBasePackages = "com.system")
public class InversionContainer {

    /**
     * Start the inversion control container
     *
     * @param args
     */
    public static void startInversion(String[] args) {
        SpringApplication.run(InversionContainer.class, args);
    }

    /**
     * The main method that can be executed to start the inversion control container
     *
     * @param args
     */
    public static void main(String[] args) {
        InversionContainer.startInversion(args);
    }
}

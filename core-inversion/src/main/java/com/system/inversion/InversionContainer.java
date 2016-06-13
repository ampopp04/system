package com.system.inversion;

import com.system.inversion.component.InversionComponent;
import com.system.inversion.intercept.SimpleClasspathMetadataReaderFactory;
import com.system.manipulator.interceptor.util.InterceptorUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import static com.system.inversion.util.InversionUtils.SYSTEM_PACKAGE_ROOT;
import static com.system.manipulator.interceptor.util.InterceptorUtils.classInterceptor;

/**
 * The <class>InversionContainer</class> is the entry point for
 * starting the inversion control process
 *
 * @author Andrew
 * @see SpringApplication
 * @see InversionComponent
 * @see InterceptorUtils#classInterceptor
 */
@SpringBootApplication(scanBasePackages = SYSTEM_PACKAGE_ROOT)
@ComponentScan(basePackages = SYSTEM_PACKAGE_ROOT,
        includeFilters = @ComponentScan.Filter(InversionComponent.class))
public class InversionContainer {

    /**
     * Start the inversion control container
     *
     * @param args
     */
    public static void startInversion(String[] args) {
        //Replace spring class loading to load and read class metadata from the class path
        classInterceptor(SimpleMetadataReaderFactory.class, SimpleClasspathMetadataReaderFactory.class);
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

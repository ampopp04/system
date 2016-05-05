package com.system;

import java.util.Arrays;
import com.system.util.date.DateUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SystemRunner {
    public static void main(String[] args) {
        System.out.println("Hello World! Today is " + DateUtils.getToday());
        ApplicationContext ctx = SpringApplication.run(SystemRunner.class, args);
        
        System.out.println("Let's inspect the beans provided by Spring Boot:");
        
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
}

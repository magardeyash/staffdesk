package com.staffdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the StaffDesk application.
 *
 * @SpringBootApplication enables:
 * 1. @Configuration: Tags the class as a source of bean definitions.
 * 2. @EnableAutoConfiguration: Tells Spring Boot to configure beans based on classpath dependencies.
 * 3. @ComponentScan: Scans for beans in com.staffdesk and all sub-packages.
 */
@SpringBootApplication
public class StaffDeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaffDeskApplication.class, args);
    }
}

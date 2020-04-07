package com.personalweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PersonalWebApplication {
    private static Logger logger = LoggerFactory.getLogger(PersonalWebApplication.class);

    public static void main(String[] args) throws Exception {
        logger.info("Current version: 1.0	Date：2020-04-03 ");
        SpringApplication.run(PersonalWebApplication.class, args);
    }

}

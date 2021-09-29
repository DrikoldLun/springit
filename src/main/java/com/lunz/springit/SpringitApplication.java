package com.lunz.springit;

import com.lunz.springit.config.SpringitProperties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(SpringitProperties.class)
public class SpringitApplication {
    private static final Logger log = LoggerFactory.getLogger(SpringitApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringitApplication.class, args);
    }
}

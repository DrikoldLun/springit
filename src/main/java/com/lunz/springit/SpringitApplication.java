package com.lunz.springit;

import com.lunz.springit.config.SpringitProperties;
import com.lunz.springit.domain.Comment;
import com.lunz.springit.domain.Link;
import com.lunz.springit.repository.CommentRepository;
import com.lunz.springit.repository.LinkRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(SpringitProperties.class)
public class SpringitApplication {
    private static final Logger log = LoggerFactory.getLogger(SpringitApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringitApplication.class, args);
    }

    //@Bean
    CommandLineRunner runner(LinkRepository linkRepository, CommentRepository commentRepository) {
        return args -> {
            Link link = new Link("Getting started with Spring Boot 2","https://therealdanvega.com/spring-boot-2");
            linkRepository.save(link);

            Comment comment = new Comment("This Spring Boot 2 link is awesome!",link);
            commentRepository.save(comment);
            link.addComment(comment);
        };
    }
}

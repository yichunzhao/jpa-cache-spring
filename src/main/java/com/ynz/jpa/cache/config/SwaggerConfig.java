package com.ynz.jpa.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket getDocket() {
        //following a builder pattern
        return new Docket(DocumentationType.SWAGGER_2)
                .select() //get the builder
                .paths(PathSelectors.ant("/api/**")) //uri paths to expose
                .apis(RequestHandlerSelectors.basePackage("com.ynz.jpa.cache.controller"))//base package that needs to be documented
                .build()
                .apiInfo(apiInfoDetails());
    }

    private ApiInfo apiInfoDetails() {
        return new ApiInfo("Author-book API",
                "Demo API for Spring Cache etc",
                "1.0",
                "Free to use",
                new Contact("Yichun ZHoa", "xxx url", "yyy@mail.com"),
                "bla bla license",
                "http://licence.url",
                Collections.emptyList()
        );
    }

}

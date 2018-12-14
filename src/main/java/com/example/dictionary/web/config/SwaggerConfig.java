package com.example.dictionary.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.dictionary.web.controller"))//путь к контроллеру для сваггера
                .build()
                .apiInfo(apiInfo());
    }
    @Value("${mysite}")
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Translate api", "Select api from the list below", "1.0", null, new springfox.documentation.service.Contact("Author", "https://perm.hh.ru/resume/d2c4faafff0403a6d50039ed1f486146713737", "hhperm.56@yandex.ru"), null, null, Collections.emptyList());
    }
}
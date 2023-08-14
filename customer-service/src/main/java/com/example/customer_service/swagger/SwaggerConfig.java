package com.example.customer_service.swagger;

import org.springframework.stereotype.Component;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/*@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.customer_service.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}*/

@Configuration
@EnableWebMvc

public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket swaggerConfiguration() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/error").negate())
                .build()
                .apiInfo(apiInfo());
        docket.useDefaultResponseMessages(false);
        return appendTags(docket);

    }
    private Docket appendTags(Docket docket) {
        return docket.tags(
                new Tag(DescriptionVariables.CUSTOMER,
                        "Used to get, create, edit, update and delete customer")
                );
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Marker Service API")
                .description("Market Service API")
                .version("1.0")
                .build();
    }

}


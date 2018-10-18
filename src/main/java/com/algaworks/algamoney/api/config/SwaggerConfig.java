package com.algaworks.algamoney.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author s2it_rboni
 * @version $Revision: $<br/>
 * $Id: $
 * @since 18/10/18 17:11
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Bean
    public Docket categoriaApi () {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.algaworks.algamoney.api"))
                .paths(springfox.documentation.builders.PathSelectors.regex("/categorias.*"))
                .build()
                .apiInfo(metaData());

    }

    private ApiInfo metaData () {
        return new ApiInfoBuilder()
                .title("REST API Algamoney")
                .description("Documentação da API REST do projeto Algamoney")
                .version("1.0.0")
                .contact(new Contact("Rodrigo Boni", "", "rodrigoboni@gmail.com"))
                .build();
    }

    @Override
    protected void addResourceHandlers (final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}

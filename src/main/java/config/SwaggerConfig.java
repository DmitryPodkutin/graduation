package config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData());
    }

    protected ApiInfo metaData() {
        return new ApiInfo(
                "Voting system REST API",
                "<ins><b>Voting system for deciding where to have lunch</b></ins>\n" +
                        "REST API using Maven/Hibernate/Spring/SpringMVC/Security\nREST(Jackson), " +
                        "Java 8 Stream and Time API Storage in databases HSQLDB\n\n" +
                        "   <ins><b>Login/Password for testing:</b></ins>  \n" +
                        "<b>ADMIN: </b> admin@gmail.com / admin  \n" +
                        "<b>USER: </b> user@yandex.ru / password  ",
                "1.0.0",
                "https://github.com/DmitryPodkutin/voting_system",
                new Contact("Podkutin Dmitry", "", "dmitry.podkutin@gmail.com"),
                " ", " ", Collections.emptyList());
    }
}


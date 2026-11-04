package br.com.navdata.flowcash.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NavFlowCash API")
                        .version("1.0.0")
                        .description("API RESTful do Sistema de Gestão Financeira NavFlowCash")
                        .contact(new Contact().name("NavData").url("https://flowcash.navdata.com.br")));
    }
}

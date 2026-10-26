package com.commerce.apirest.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Commerce API", version = "1.0", description = "Documentação da Commerce API"))
public class SwaggerConfig {

}

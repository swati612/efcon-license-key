package com.nxtlife.efkon.license.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {

		Map<String,SecurityScheme> securitySchemeMap =new HashMap<String, SecurityScheme>();
		SecurityScheme securityScheme= new SecurityScheme();
		securityScheme.setType(Type.HTTP);
		securityScheme.setScheme("bearer");
		securitySchemeMap.put("BearerAuth", securityScheme);
		SecurityRequirement securityRequirement= new SecurityRequirement();
		//securityRequirement.addList("ApiKeyAuth");
		securityRequirement.addList("BearerAuth");
		return new OpenAPI().security(Arrays.asList(securityRequirement)).components(new Components().securitySchemes(securitySchemeMap))
				.info(new Info().title("License Key API")
						.description("License Key Rest Api Documentation").version("v1")
						.contact(new Contact().email("ajay_garg@nxtlifetechnologies.com").name("Ajay Garg")
								.url("http://www.nxtlifetechnologies.com")));
	}
}
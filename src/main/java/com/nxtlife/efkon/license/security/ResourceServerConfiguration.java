package com.nxtlife.efkon.license.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import com.nxtlife.efkon.license.ex.oauth.CustomAuthenticationEntryPoint;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	private static final String SECURED_READ_SCOPE = "#oauth2.hasScope('read')";
	private static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('write')";
	private static final String SECURED_PATTERN = "/api/**";
	private static final String FORGOT_PASSWORD_PATTERN = "/forgot-password";

	@Autowired
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId("license-key-api");
		resources.authenticationEntryPoint(customAuthenticationEntryPoint);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.requestMatchers().antMatchers("/actuator/*").antMatchers("/v3/api-docs").antMatchers("/swagger-ui.html").antMatchers("/swagger-ui/*").antMatchers(FORGOT_PASSWORD_PATTERN).antMatchers(FORGOT_PASSWORD_PATTERN + "/*")
				.antMatchers(SECURED_PATTERN).antMatchers("/*").and().authorizeRequests()
				.antMatchers(FORGOT_PASSWORD_PATTERN).permitAll().antMatchers("/v3/api-docs").permitAll().antMatchers("/actuator/*").permitAll()
				.antMatchers("/swagger-ui/*").permitAll().antMatchers("/swagger-ui.html").permitAll().antMatchers(FORGOT_PASSWORD_PATTERN + "/*")
				.permitAll().antMatchers(HttpMethod.POST, SECURED_PATTERN).access(SECURED_WRITE_SCOPE).anyRequest()
				.access(SECURED_READ_SCOPE);
	}
}

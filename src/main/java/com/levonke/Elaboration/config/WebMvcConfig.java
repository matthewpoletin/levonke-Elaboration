package com.levonke.Elaboration.config;

import com.levonke.Elaboration.interceptor.TokenAuthenticationInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	private final TokenAuthenticationInterceptor tokenAuthenticationInterceptor;
	
	@Autowired
	public WebMvcConfig(TokenAuthenticationInterceptor tokenAuthenticationInterceptor) {
		this.tokenAuthenticationInterceptor = tokenAuthenticationInterceptor;
	}
	
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tokenAuthenticationInterceptor);
	}
}
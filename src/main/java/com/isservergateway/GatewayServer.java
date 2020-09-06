/**
 * 
 */
package com.isservergateway;

import com.isservergateway.filter.AuditLogFilter;
import com.isservergateway.filter.AuthorizationFilter;
import com.isservergateway.filter.OauthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jojo
 *
 */
@SpringBootApplication
@EnableZuulProxy
public class GatewayServer {

	/**
	 * 
	 * @author jojo
	 * 2019年8月18日
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(GatewayServer.class, args);
	}

	@Bean
    public OauthFilter oauthFilter(){
	    return new OauthFilter();
    }

    @Bean
    public AuthorizationFilter authorizationFilter(){
	    return new AuthorizationFilter();
    }

    @Bean
    public AuditLogFilter auditLogFilter(){
	    return new AuditLogFilter();
    }



}

package com.isservergateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sun.javaws.progress.PreloaderPostEventListener;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.RequestContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class OauthFilter extends ZuulFilter {


    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public String filterType() {
        return "pre";//pre post error route
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }



    @Override
    public Object run() throws ZuulException {
        System.out.println("oauth start!");

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        if(StringUtils.startsWith(request.getRequestURI(),"/token")){
            return null;
        }

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.isBlank(authHeader)){
            return null;
        }

        if(!StringUtils.startsWithIgnoreCase(authHeader,"bearer ")){
            return null;
        }

        try{
            TokenInfo info = getTokenInfo(authHeader);
            request.setAttribute("tokenInfo",info);
        }catch(Exception e){
            System.out.println("get token info fail");
        }


        return null;
    }

    private TokenInfo getTokenInfo(String authHeader) {
        String token = StringUtils.substringAfter(authHeader,"bearer ");
        String oauthServiceUrl = "http://127.0.0.1:8762/oauth/check_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("gateway","123456");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("token",token);

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,headers);

        ResponseEntity<TokenInfo> response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);

        System.out.println("token info ："+response.getBody().toString());

        return response.getBody();

    }
}

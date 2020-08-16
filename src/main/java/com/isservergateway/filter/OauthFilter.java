package com.isservergateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.RequestContent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@Component
public class OauthFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";//pre post error route
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("oauth start!");

        RequestContext requestContext = new RequestContext();
        HttpServletRequest request = requestContext.getRequest();

        if(StringUtils.startsWith(request.getRequestURI(),"/token")){
            return null;
        }

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.isBlank(authHeader)){
            return null;
        }

        if(!StringUtils.startsWithIgnoreCase("authHeader","bearer ")){
            return null;
        }

        try{
            TokenInfo info = getTokenInfo(authHeader);
            request.setAttribute("tokenInfo",info);
        }catch(Exception e){

        }


        return null;
    }

    private TokenInfo getTokenInfo(String authHeader) {
        String token = StringUtils.substringAfter(authHeader,"bearer ");
        String oauthServiceUrl = "http://127.0.0.1:8762/oauth//check_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("gateway","123456");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("token",token);

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,headers);

        //ResponseEntity<TokenInfo> response =



        return getTokenInfo(authHeader);
    }
}

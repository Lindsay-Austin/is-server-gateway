package com.isservergateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HttpServletBean;

import javax.servlet.http.HttpServletRequest;

public class AuthorizationFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {

        return true;
    }

    /**
     * 此方法判断当前请求的url是否需要鉴权，可以查询数据库等判断
     * 即在数据库的表中配上url的权限
     * @param request
     * @return
     */
    private boolean isNeedAuth(HttpServletRequest request) {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("authorization start   !!!");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if(isNeedAuth(request)) {

            TokenInfo tokenInfo = (TokenInfo)request.getAttribute("tokenInfo");

            if(tokenInfo != null && tokenInfo.isActive()) {
                if(!hasPermission(tokenInfo, request)) {
                    System.out.println("audit log update fail 403");
                    handleError(403, requestContext);
                }

                requestContext.addZuulRequestHeader("username", tokenInfo.getUser_name());
            }else {
                if(!StringUtils.startsWith(request.getRequestURI(), "/token")) {
                    System.out.println("audit log update fail 401");
                    handleError(401, requestContext);
                }
            }
        }
        return null;
    }

    private void handleError(int status, RequestContext requestContext) {
        requestContext.getResponse().setContentType("application/json");
        requestContext.setResponseStatusCode(status);
        requestContext.setResponseBody("{\"message\":\"auth fail\"}");
        requestContext.setSendZuulResponse(false);
    }

    private boolean hasPermission(TokenInfo tokenInfo, HttpServletRequest request) {
        //return RandomUtils.nextInt() % 2 == 0;
        return true; //RandomUtils.nextInt() % 2 == 0;
    }
}

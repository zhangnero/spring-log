package com.nero.spring.log.config;

import com.nero.spring.log.config.wrapper.RequestBodyReadWrapper;
import com.nero.spring.log.config.wrapper.ResponseBodyReadWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Configuration
@WebFilter(filterName = "LogFilter", urlPatterns = "/*")
public class LoggerFilter implements Filter {

    public static final Logger logger = LoggerFactory.getLogger("RequestLog");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean isRequestJson = request.getContentType() == null || request.getContentType().contains("application/json");

        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Request Begin {} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", new Date());
        logger.info("Request URI: {}",((HttpServletRequest)request).getRequestURI());
        logger.info("Request Method: {}",((HttpServletRequest)request).getMethod());

        Map<String,String[]> params = request.getParameterMap();
        if (params != null && !params.isEmpty()){
            StringBuilder paramsString = new StringBuilder();
            params.forEach((name, values) -> {
                paramsString.append(name);
                paramsString.append(": ");
                for (int i = 0; i < values.length; i++) {
                    paramsString.append(values[i]);
                    if (i == values.length - 1){
                        paramsString.append("; ");
                    }else {
                        paramsString.append(",");
                    }
                }
            });
            logger.info("Request Params: {}", paramsString.toString());;
        }


        if (isRequestJson){
            request = new RequestBodyReadWrapper((HttpServletRequest) request);

            logger.info("Request Body: {}", ((RequestBodyReadWrapper)request).getBody());
        }

        response = new ResponseBodyReadWrapper((HttpServletResponse) response);

        chain.doFilter(request, response);

        logger.info("Response Body: {}", new String(((ResponseBodyReadWrapper)response).getBackup(), StandardCharsets.UTF_8));


        logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Request End {} <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<", new Date());

    }
}

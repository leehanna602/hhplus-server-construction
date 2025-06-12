package com.hhplus.server.interfaces.support;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


@Component
@Slf4j
public class RequestLoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // requestId 생성
        String requestId = UUID.randomUUID().toString();
        try {
            MDC.put("request_id", requestId);

            log.info("Request - Method: {} | URL: {} | Client IP: {} | User Agent: {} | Time: {}",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURL(),
                    httpRequest.getRemoteAddr(),
                    httpRequest.getHeader("User-Agent"),
                    LocalDateTime.now()
            );

            filterChain.doFilter(servletRequest, servletResponse);
            log.info("Response Status: {}", httpResponse.getStatus());

        } finally {
            // 요청 완료 후 MDC clear
            MDC.clear();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }


}

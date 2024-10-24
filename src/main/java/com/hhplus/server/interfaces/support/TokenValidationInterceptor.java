package com.hhplus.server.interfaces.support;

import com.hhplus.server.domain.common.exception.WaitingQueueErrorCode;
import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.waitingQueue.WaitingQueueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenValidationInterceptor implements HandlerInterceptor {

    private final WaitingQueueService waitingQueueService;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws IOException {

        if ((request.getMethod().equals("POST") && request.getRequestURI().contains("/v1/payment"))
            || (request.getMethod().equals("POST") && request.getRequestURI().contains("/v1/reservation/seat"))) {
            String token = request.getHeader("token");
            if (!isValidReservation(token)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid reservation");
                return false;
            }
        }
        return true;
    }

    private boolean isValidReservation(String token) {
        boolean validateActiveToken = waitingQueueService.validateActiveToken(token);
        if (!validateActiveToken) {
            throw new CommonException(WaitingQueueErrorCode.INVALID_TOKEN);
        }
        return true;
    }

}

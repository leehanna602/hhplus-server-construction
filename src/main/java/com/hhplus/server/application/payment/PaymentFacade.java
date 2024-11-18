package com.hhplus.server.application.payment;

import com.hhplus.server.domain.payment.PaymentService;
import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.domain.user.UserService;
import com.hhplus.server.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final UserService userService;
    private final PaymentService paymentService;

    public PaymentInfo concertPayment(Long userId, String token, Long reservationId) {
        User user = userService.getUser(userId);
        PaymentInfo paymentInfo = paymentService.payment(user, token, reservationId);
        return paymentInfo;
    }

}

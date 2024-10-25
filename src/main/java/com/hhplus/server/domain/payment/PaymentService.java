package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.concert.model.Reservation;
import com.hhplus.server.domain.payment.model.Payment;
import com.hhplus.server.domain.payment.model.PaymentStatus;
import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentReader paymentReader;
    private final PaymentWriter paymentWriter;


    @Transactional
    public void completePayment(User user, Reservation reservation, Point point) {
        Payment payment = new Payment(user, reservation, reservation.getSeat().getPrice(), PaymentStatus.COMPLETED);
        paymentWriter.save(payment);
    }

}

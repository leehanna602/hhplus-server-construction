package com.hhplus.server.interfaces.v1.payment;

import com.hhplus.server.application.payment.PaymentFacade;
import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.interfaces.v1.payment.req.PaymentReq;
import com.hhplus.server.interfaces.v1.payment.res.PaymentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    /* 결제 */
    @PostMapping("")
    public ResponseEntity<PaymentRes> payment(@RequestHeader("token") String token, @RequestBody PaymentReq paymentReq) {
        PaymentInfo paymentInfo = paymentFacade.concertPayment(paymentReq.userId(), paymentReq.token(), paymentReq.reservationId());
        return ResponseEntity.ok(new PaymentRes(paymentInfo.userId(), paymentInfo.status()));
    }

}

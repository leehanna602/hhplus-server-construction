package com.hhplus.server.interfaces.v1.payment;

import com.hhplus.server.domain.payment.model.PaymentStatus;
import com.hhplus.server.interfaces.v1.payment.req.PaymentReq;
import com.hhplus.server.interfaces.v1.payment.res.PaymentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class PaymentController {

    /* 결제 */
    @PostMapping("")
    public ResponseEntity<PaymentRes> payment(@RequestBody PaymentReq paymentReq) {
        return ResponseEntity.ok(new PaymentRes(paymentReq.userId(), PaymentStatus.COMPLETED));
    }

}

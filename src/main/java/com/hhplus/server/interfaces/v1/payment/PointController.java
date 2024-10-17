package com.hhplus.server.interfaces.v1.payment;

import com.hhplus.server.interfaces.v1.payment.req.ChargePointReq;
import com.hhplus.server.interfaces.v1.payment.res.PointRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/point")
public class PointController {

    /* 포인트 조회 */
    @GetMapping("/{userId}")
    public ResponseEntity<PointRes> getPoint(@PathVariable Long userId) {
        return ResponseEntity.ok(new PointRes(userId, 35000));
    }

    /* 포인트 충전 */
    @PostMapping("/{userId}")
    public ResponseEntity<PointRes> chargePoint(@RequestBody ChargePointReq chargePointReq) {
        return ResponseEntity.ok(new PointRes(chargePointReq.userId(), 50000));
    }

}

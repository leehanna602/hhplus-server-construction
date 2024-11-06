package com.hhplus.server.interfaces.v1.payment;

import com.hhplus.server.application.payment.PointFacade;
import com.hhplus.server.domain.payment.dto.UserPointInfo;
import com.hhplus.server.interfaces.v1.payment.req.ChargePointReq;
import com.hhplus.server.interfaces.v1.payment.res.PointRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/point")
public class PointController {

    private final PointFacade pointFacade;

    /* 포인트 조회 */
    @GetMapping("/{userId}")
    public ResponseEntity<PointRes> getPoint(@PathVariable Long userId) {
        UserPointInfo userPointInfo = pointFacade.getUserPointInfo(userId);
        return ResponseEntity.ok(new PointRes(userId, userPointInfo.point()));
    }

    /* 포인트 충전 */
    @PostMapping("/{userId}")
    public ResponseEntity<PointRes> chargePoint(@RequestBody ChargePointReq chargePointReq) {
        UserPointInfo userPointInfo = pointFacade.pointTransaction(chargePointReq.userId(), chargePointReq.amount(), chargePointReq.type());
        return ResponseEntity.ok(new PointRes(chargePointReq.userId(), userPointInfo.point()));
    }

}

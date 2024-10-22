package com.hhplus.server.interfaces.v1.waitingQueue;

import com.hhplus.server.application.waitingQueue.WaitingQueueFacade;
import com.hhplus.server.domain.waitingQueue.dto.WaitingQueueInfo;
import com.hhplus.server.interfaces.v1.waitingQueue.req.UserTokenReq;
import com.hhplus.server.interfaces.v1.waitingQueue.res.UserTokenRes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/waiting-queue")
public class QueueController {

    private final WaitingQueueFacade waitingQueueFacade;

    /* 토큰 발급 및 조회 */
    @PostMapping("/status")
    @Operation(summary = "콘서트 예약을 위한 사용자 토큰 발급 및 조회", description = "콘서트 예약을 위한 사용자 토큰 발급 및 조회 API")
    public ResponseEntity<UserTokenRes> getToken(@RequestBody UserTokenReq userTokenReq) {
        WaitingQueueInfo waitingQueueInfo = waitingQueueFacade.getQueueStatus(userTokenReq.token());
        UserTokenRes userTokenRes = new UserTokenRes(waitingQueueInfo.token(), waitingQueueInfo.progress(), waitingQueueInfo.waitingNum());
        return ResponseEntity.ok(userTokenRes);
    }

}

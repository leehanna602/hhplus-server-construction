package com.hhplus.server.interfaces.v1.waitingQueue;

import com.hhplus.server.application.waitingQueue.WaitingQueueFacade;
import com.hhplus.server.domain.waitingQueue.dto.WaitingQueueInfo;
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

    /* 토큰 발급 */
    @PostMapping("/token")
    @Operation(summary = "콘서트 예약을 위한 사용자 토큰 발급", description = "콘서트 예약을 위한 사용자 토큰 발급 API")
    public ResponseEntity<UserTokenRes> getToken() {
        WaitingQueueInfo waitingQueueInfo = waitingQueueFacade.generateToken();
        UserTokenRes userTokenRes = new UserTokenRes(waitingQueueInfo.token(), waitingQueueInfo.progress(), waitingQueueInfo.waitingNum());
        return ResponseEntity.ok(userTokenRes);
    }

    /* 토큰 조회 */
    @GetMapping("/token")
    @Operation(summary = "콘서트 예약을 위한 사용자 토큰 상태 조회", description = "콘서트 예약을 위한 사용자 토큰 상태 조회 폴링용 API")
    public ResponseEntity<UserTokenRes> getToken(@RequestHeader("token") String token) {
        WaitingQueueInfo waitingQueueInfo = waitingQueueFacade.getTokenStatus(token);
        UserTokenRes userTokenRes = new UserTokenRes(waitingQueueInfo.token(), waitingQueueInfo.progress(), waitingQueueInfo.waitingNum());
        return ResponseEntity.ok(userTokenRes);
    }

}

package com.hhplus.server.interfaces.v1.waitingQueue;

import com.hhplus.server.interfaces.v1.waitingQueue.req.UserTokenReq;
import com.hhplus.server.interfaces.v1.waitingQueue.res.UserTokenRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/waiting-queue")
public class QueueController {

    /* 토큰 발급 및 조회 */
    @PostMapping("/status")
    public ResponseEntity<UserTokenRes> getToken(@RequestBody UserTokenReq userTokenReq) {
        return ResponseEntity.ok(null);
    }

}

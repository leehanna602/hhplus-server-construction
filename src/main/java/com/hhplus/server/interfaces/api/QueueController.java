package com.hhplus.server.interfaces.api;

import com.hhplus.server.interfaces.dto.req.GenerateTokenReq;
import com.hhplus.server.interfaces.dto.req.UserTokenReq;
import com.hhplus.server.interfaces.dto.res.UserTokenRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/queue")
public class QueueController {

    /* 토큰 발급 */
    @PostMapping("/token")
    public ResponseEntity<UserTokenRes> generateToken(@RequestBody GenerateTokenReq generateTokenReq) {
        return ResponseEntity.ok(new UserTokenRes(1952L, "eyJhbGciOiJIUzI1", 598L, 300L));
    }

    /* 대기상태 확인 */
    @GetMapping("/status")
    public ResponseEntity<UserTokenRes> userQueueStatus(@RequestBody UserTokenReq userTokenReq) {
        return ResponseEntity.ok(new UserTokenRes(1952L, "eyJhbGciOiJIUzI1", 598L, 300L));
    }

}

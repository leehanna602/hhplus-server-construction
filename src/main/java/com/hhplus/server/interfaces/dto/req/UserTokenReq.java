package com.hhplus.server.interfaces.dto.req;

public record UserTokenReq (
        Long userId,
        String token
){
}

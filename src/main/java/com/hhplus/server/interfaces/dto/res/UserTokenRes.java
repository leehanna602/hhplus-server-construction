package com.hhplus.server.interfaces.dto.res;

public record UserTokenRes (
        Long userId,
        String token,
        Long waitNum,
        Long waitTime
) {
}

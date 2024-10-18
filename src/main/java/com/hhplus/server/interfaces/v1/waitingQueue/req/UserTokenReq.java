package com.hhplus.server.interfaces.v1.waitingQueue.req;

import jakarta.annotation.Nullable;

public record UserTokenReq (
        @Nullable String token
){
}

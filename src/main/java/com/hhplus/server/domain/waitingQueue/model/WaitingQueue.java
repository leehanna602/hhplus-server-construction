package com.hhplus.server.domain.waitingQueue.model;

import java.util.UUID;

public record WaitingQueue(
        String token,
        long expirationTimeMillis
){

    public static WaitingQueue of() {
        return new WaitingQueue(UUID.randomUUID().toString(),
                System.currentTimeMillis());
    }

}

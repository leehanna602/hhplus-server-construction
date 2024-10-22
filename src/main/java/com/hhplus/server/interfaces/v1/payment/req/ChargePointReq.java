package com.hhplus.server.interfaces.v1.payment.req;

import com.hhplus.server.domain.payment.model.TransactionType;

public record ChargePointReq(
        Long userId,
        TransactionType type,
        int amount
) {
}

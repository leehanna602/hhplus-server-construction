package com.hhplus.server.interfaces.dto.req;

import com.hhplus.server.domain.payment.TransactionType;

public record ChargePointReq(
        Long userId,
        TransactionType type,
        int amount
) {
}

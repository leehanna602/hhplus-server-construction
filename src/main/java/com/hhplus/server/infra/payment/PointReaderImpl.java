package com.hhplus.server.infra.payment;

import com.hhplus.server.domain.payment.PointReader;
import com.hhplus.server.domain.payment.model.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointReaderImpl implements PointReader {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public Point findByUserId(Long userId) {
        return pointJpaRepository.findByUser(userId);
    }
}

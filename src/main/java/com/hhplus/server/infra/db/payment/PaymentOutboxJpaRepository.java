package com.hhplus.server.infra.db.payment;

import com.hhplus.server.domain.payment.model.PaymentOutbox;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutbox, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    PaymentOutbox findByPaymentId(Long paymentId);

}

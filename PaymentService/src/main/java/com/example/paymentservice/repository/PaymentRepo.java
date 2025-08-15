package com.example.paymentservice.repository;

import com.example.paymentservice.domain.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    Two concurrent requests for the same orderId might both “not find” a row and both try to insert.
    @Query("select p from Payment p where p.orderId = :orderId")
    Optional<Payment> findForUpdateByOrderId(@Param("orderId") UUID orderId);

    List<Payment> findByOrderId(UUID orderId);
    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);
}

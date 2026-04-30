package com.n11.paymentservice.repository;

import com.n11.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUserEmail(String userEmail);

    List<Payment> findByOrderId(Long orderId);
}
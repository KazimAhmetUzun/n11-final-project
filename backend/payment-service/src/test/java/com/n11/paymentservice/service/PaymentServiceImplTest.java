package com.n11.paymentservice.service;

import com.n11.paymentservice.client.IyzicoPaymentClient;
import com.n11.paymentservice.entity.Payment;
import com.n11.paymentservice.enums.PaymentStatus;
import com.n11.paymentservice.publisher.PaymentResultPublisher;
import com.n11.paymentservice.repository.PaymentRepository;
import com.n11.paymentservice.request.BuyerRequest;
import com.n11.paymentservice.request.PaymentCardRequest;
import com.n11.paymentservice.request.PaymentRequest;
import com.n11.paymentservice.response.PaymentResponse;
import com.n11.paymentservice.service.impl.PaymentServiceImpl;
import com.n11.paymentservice.event.PaymentResultEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentResultPublisher paymentResultPublisher;

    @Mock
    private IyzicoPaymentClient iyzicoPaymentClient;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void pay_WhenIyzicoPaymentSuccess_ShouldSavePaymentPublishEventAndReturnSuccessResponse() {
        PaymentRequest request = createPaymentRequest();

        com.iyzipay.model.Payment iyzicoPayment = new com.iyzipay.model.Payment();
        iyzicoPayment.setStatus("success");
        iyzicoPayment.setPaymentId("iyzico-payment-id");

        Payment savedPayment = Payment.builder()
                .id(1L)
                .orderId(request.getOrderId())
                .userEmail(request.getUserEmail())
                .amount(request.getAmount())
                .transactionId("iyzico-payment-id")
                .status(PaymentStatus.SUCCESS)
                .errorMessage(null)
                .createdAt(LocalDateTime.now())
                .build();

        when(iyzicoPaymentClient.createPayment(request)).thenReturn(iyzicoPayment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponse response = paymentService.pay(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getOrderId());
        assertEquals("kazim@test.com", response.getUserEmail());
        assertEquals(BigDecimal.valueOf(10.00), response.getAmount());
        assertEquals("iyzico-payment-id", response.getTransactionId());
        assertEquals(PaymentStatus.SUCCESS, response.getStatus());
        assertNull(response.getErrorMessage());

        verify(iyzicoPaymentClient, times(1)).createPayment(request);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(paymentResultPublisher, times(1)).publish(any(PaymentResultEvent.class));
    }

    @Test
    void pay_WhenIyzicoPaymentFailed_ShouldSavePaymentPublishEventAndReturnFailedResponse() {
        PaymentRequest request = createPaymentRequest();

        com.iyzipay.model.Payment iyzicoPayment = new com.iyzipay.model.Payment();
        iyzicoPayment.setStatus("failure");
        iyzicoPayment.setPaymentId(null);
        iyzicoPayment.setErrorMessage("Payment failed");

        Payment savedPayment = Payment.builder()
                .id(1L)
                .orderId(request.getOrderId())
                .userEmail(request.getUserEmail())
                .amount(request.getAmount())
                .transactionId(null)
                .status(PaymentStatus.FAILED)
                .errorMessage("Payment failed")
                .createdAt(LocalDateTime.now())
                .build();

        when(iyzicoPaymentClient.createPayment(request)).thenReturn(iyzicoPayment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponse response = paymentService.pay(request);

        assertNotNull(response);
        assertEquals(PaymentStatus.FAILED, response.getStatus());
        assertEquals("Payment failed", response.getErrorMessage());

        verify(iyzicoPaymentClient, times(1)).createPayment(request);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(paymentResultPublisher, times(1)).publish(any(PaymentResultEvent.class));
    }

    @Test
    void getByUserEmail_ShouldReturnPayments() {
        Payment payment = createPayment();

        when(paymentRepository.findByUserEmail("kazim@test.com"))
                .thenReturn(List.of(payment));

        List<PaymentResponse> response = paymentService.getByUserEmail("kazim@test.com");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getId());
        assertEquals(1L, response.get(0).getOrderId());
        assertEquals("kazim@test.com", response.get(0).getUserEmail());
        assertEquals(BigDecimal.valueOf(10.00), response.get(0).getAmount());
        assertEquals("iyzico-payment-id", response.get(0).getTransactionId());
        assertEquals(PaymentStatus.SUCCESS, response.get(0).getStatus());
        assertNull(response.get(0).getErrorMessage());

        verify(paymentRepository, times(1)).findByUserEmail("kazim@test.com");
    }

    @Test
    void getByOrderId_ShouldReturnPayments() {
        Payment payment = createPayment();

        when(paymentRepository.findByOrderId(1L))
                .thenReturn(List.of(payment));

        List<PaymentResponse> response = paymentService.getByOrderId(1L);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getOrderId());
        assertEquals(PaymentStatus.SUCCESS, response.get(0).getStatus());

        verify(paymentRepository, times(1)).findByOrderId(1L);
    }

    @Test
    void getByUserEmail_WhenNoPaymentExists_ShouldReturnEmptyList() {
        when(paymentRepository.findByUserEmail("empty@test.com"))
                .thenReturn(List.of());

        List<PaymentResponse> response = paymentService.getByUserEmail("empty@test.com");

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(paymentRepository, times(1)).findByUserEmail("empty@test.com");
    }

    @Test
    void getByOrderId_WhenNoPaymentExists_ShouldReturnEmptyList() {
        when(paymentRepository.findByOrderId(99L))
                .thenReturn(List.of());

        List<PaymentResponse> response = paymentService.getByOrderId(99L);

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(paymentRepository, times(1)).findByOrderId(99L);
    }

    private PaymentRequest createPaymentRequest() {
        PaymentCardRequest card = new PaymentCardRequest();
        card.setCardHolderName("John Doe");
        card.setCardNumber("5528790000000008");
        card.setExpireMonth("12");
        card.setExpireYear("2030");
        card.setCvc("123");

        BuyerRequest buyer = new BuyerRequest();
        buyer.setName("Kazim");
        buyer.setSurname("Uzun");
        buyer.setEmail("kazim@test.com");
        buyer.setIdentityNumber("11111111111");
        buyer.setPhone("+905350000000");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        buyer.setAddress("Test address");
        buyer.setZipCode("34000");

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setUserEmail("kazim@test.com");
        request.setAmount(BigDecimal.valueOf(10.00));
        request.setCard(card);
        request.setBuyer(buyer);

        return request;
    }

    private Payment createPayment() {
        return Payment.builder()
                .id(1L)
                .orderId(1L)
                .userEmail("kazim@test.com")
                .amount(BigDecimal.valueOf(10.00))
                .transactionId("iyzico-payment-id")
                .status(PaymentStatus.SUCCESS)
                .errorMessage(null)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
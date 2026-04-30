package com.n11.paymentservice.client;

import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import com.n11.paymentservice.request.BuyerRequest;
import com.n11.paymentservice.request.PaymentCardRequest;
import com.n11.paymentservice.request.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class IyzicoPaymentClient {

    private final Options iyzicoOptions;

    public Payment createPayment(PaymentRequest request) {
        return Payment.create(createIyzicoPaymentRequest(request), iyzicoOptions);
    }

    private CreatePaymentRequest createIyzicoPaymentRequest(PaymentRequest request) {
        CreatePaymentRequest iyzicoRequest = new CreatePaymentRequest();

        iyzicoRequest.setLocale(Locale.TR.getValue());
        iyzicoRequest.setConversationId(String.valueOf(request.getOrderId()));
        iyzicoRequest.setPrice(request.getAmount());
        iyzicoRequest.setPaidPrice(request.getAmount());
        iyzicoRequest.setCurrency(Currency.TRY.name());
        iyzicoRequest.setInstallment(1);
        iyzicoRequest.setBasketId("ORDER-" + request.getOrderId());
        iyzicoRequest.setPaymentChannel(PaymentChannel.WEB.name());
        iyzicoRequest.setPaymentGroup(PaymentGroup.PRODUCT.name());

        iyzicoRequest.setPaymentCard(toIyzicoPaymentCard(request.getCard()));
        iyzicoRequest.setBuyer(toIyzicoBuyer(request.getBuyer()));
        iyzicoRequest.setShippingAddress(toIyzicoAddress(request.getBuyer()));
        iyzicoRequest.setBillingAddress(toIyzicoAddress(request.getBuyer()));
        iyzicoRequest.setBasketItems(Collections.singletonList(toBasketItem(request)));

        return iyzicoRequest;
    }

    private PaymentCard toIyzicoPaymentCard(PaymentCardRequest request) {
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName(request.getCardHolderName());
        paymentCard.setCardNumber(request.getCardNumber());
        paymentCard.setExpireMonth(request.getExpireMonth());
        paymentCard.setExpireYear(request.getExpireYear());
        paymentCard.setCvc(request.getCvc());
        paymentCard.setRegisterCard(0);
        return paymentCard;
    }

    private Buyer toIyzicoBuyer(BuyerRequest request) {
        Buyer buyer = new Buyer();
        buyer.setId(request.getEmail());
        buyer.setName(request.getName());
        buyer.setSurname(request.getSurname());
        buyer.setGsmNumber(request.getPhone());
        buyer.setEmail(request.getEmail());
        buyer.setIdentityNumber(request.getIdentityNumber());
        buyer.setLastLoginDate("2026-04-29 12:00:00");
        buyer.setRegistrationDate("2026-04-29 12:00:00");
        buyer.setRegistrationAddress(request.getAddress());
        buyer.setIp("127.0.0.1");
        buyer.setCity(request.getCity());
        buyer.setCountry(request.getCountry());
        buyer.setZipCode(request.getZipCode());
        return buyer;
    }

    private Address toIyzicoAddress(BuyerRequest request) {
        Address address = new Address();
        address.setContactName(request.getName() + " " + request.getSurname());
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setAddress(request.getAddress());
        address.setZipCode(request.getZipCode());
        return address;
    }

    private BasketItem toBasketItem(PaymentRequest request) {
        BasketItem basketItem = new BasketItem();
        basketItem.setId("BI-" + request.getOrderId());
        basketItem.setName("Order Payment");
        basketItem.setCategory1("E-Commerce");
        basketItem.setItemType(BasketItemType.PHYSICAL.name());
        basketItem.setPrice(request.getAmount());
        return basketItem;
    }
}
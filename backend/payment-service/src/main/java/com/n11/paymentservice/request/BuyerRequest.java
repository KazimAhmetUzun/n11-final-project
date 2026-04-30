package com.n11.paymentservice.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyerRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String identityNumber;

    @NotBlank
    private String phone;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private String address;

    @NotBlank
    private String zipCode;
}
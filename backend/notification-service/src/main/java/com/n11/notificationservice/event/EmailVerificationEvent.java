package com.n11.notificationservice.event;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerificationEvent {

    private String email;
    private String fullName;
    private String verificationCode;
}
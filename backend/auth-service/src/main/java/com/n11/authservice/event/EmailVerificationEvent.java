package com.n11.authservice.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationEvent {

    private String email;
    private String fullName;
    private String verificationCode;
}
package com.restclientdemo.send_message_service.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneBook {
    private long id;
    private String userName;
    private String phoneNumber;
}

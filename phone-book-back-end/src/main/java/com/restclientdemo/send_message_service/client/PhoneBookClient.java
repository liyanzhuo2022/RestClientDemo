package com.restclientdemo.send_message_service.client;

import java.util.List;

import com.restclientdemo.send_message_service.domain.PhoneBook;
import com.restclientdemo.send_message_service.domain.PhoneBookDto;

public interface PhoneBookClient {
    PhoneBook createPhoneBook(PhoneBookDto phoneBookDto);

    List<PhoneBook> getPhoneBooks();

    PhoneBook updatePhoneBook(long id, PhoneBookDto phoneBookDto);

    void deletePhoneBook(long id);
}

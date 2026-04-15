package com.restclientdemo.echo_service.client;

import java.util.List;

import com.restclientdemo.echo_service.domain.PhoneBook;
import com.restclientdemo.echo_service.domain.PhoneBookDto;

public interface PhoneBookClient {
    PhoneBook createPhoneBook(PhoneBookDto phoneBookDto);

    List<PhoneBook> getPhoneBooks();

    PhoneBook updatePhoneBook(long id, PhoneBookDto phoneBookDto);

    void deletePhoneBook(long id);
}

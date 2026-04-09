package com.restclientdemo.send_message_service.core;

import java.util.List;

import org.springframework.stereotype.Service;

import com.restclientdemo.send_message_service.client.PhoneBookClient;
import com.restclientdemo.send_message_service.domain.PhoneBook;
import com.restclientdemo.send_message_service.domain.PhoneBookDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhoneBookService {
    private final PhoneBookClient phoneBookClient;

    public PhoneBook createPhoneBook(PhoneBookDto phoneBookDto) {
        return phoneBookClient.createPhoneBook(phoneBookDto);
    }

    public List<PhoneBook> getPhoneBooks() {
        return phoneBookClient.getPhoneBooks();
    }

    public PhoneBook updatePhoneBook(long id, PhoneBookDto phoneBookDto) {
        return phoneBookClient.updatePhoneBook(id, phoneBookDto);
    }

    public void deletePhoneBook(long id) {
        phoneBookClient.deletePhoneBook(id);
    }
}

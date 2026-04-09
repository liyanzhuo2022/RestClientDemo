package com.restclientdemo.send_message_service.clientrest;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.restclientdemo.send_message_service.client.PhoneBookClient;
import com.restclientdemo.send_message_service.domain.PhoneBook;
import com.restclientdemo.send_message_service.domain.PhoneBookDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.phone-book-client.type", havingValue = "HTTP_CLIENT", matchIfMissing = true)
@RequiredArgsConstructor
public class PhoneBookHttpRestClient implements PhoneBookClient {
    private final PhoneBookHttpClient httpInterface;

    @Override
    public PhoneBook createPhoneBook(PhoneBookDto phoneBookDto) {
        return httpInterface.createPhoneBook(phoneBookDto);
    }

    @Override
    public List<PhoneBook> getPhoneBooks() {
        return httpInterface.getPhoneBooks();
    }

    @Override
    public PhoneBook updatePhoneBook(long id, PhoneBookDto phoneBookDto) {
        return httpInterface.updatePhoneBook(id, phoneBookDto);
    }

    @Override
    public void deletePhoneBook(long id) {
        httpInterface.deletePhoneBook(id);
    }
}

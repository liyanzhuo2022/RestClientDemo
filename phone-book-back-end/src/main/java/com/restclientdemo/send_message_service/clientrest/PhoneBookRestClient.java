package com.restclientdemo.send_message_service.clientrest;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.restclientdemo.send_message_service.client.PhoneBookClient;
import com.restclientdemo.send_message_service.domain.PhoneBook;
import com.restclientdemo.send_message_service.domain.PhoneBookDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.phone-book-client.type", havingValue = "REST_CLIENT", matchIfMissing = true)
public class PhoneBookRestClient implements PhoneBookClient {
    private final RestClient restClient;

    PhoneBookRestClient(RestClient.Builder restClientBuilder) {
        log.info("Creating PhoneBookRestClient");

        restClient = restClientBuilder
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Override
    public PhoneBook createPhoneBook(PhoneBookDto phoneBookDto) {
        return restClient.post()
                .uri("/phone_book")
                .body(phoneBookDto)
                .retrieve()
                .body(PhoneBook.class);
    }

    @Override
    public List<PhoneBook> getPhoneBooks() {
        return restClient.get()
                .uri("/phone_book")
                .retrieve()
                .body(new ParameterizedTypeReference<List<PhoneBook>>() {});
    }

    @Override
    public PhoneBook updatePhoneBook(long id, PhoneBookDto phoneBookDto) {
        return restClient.put()
                .uri("/phone_book/{id}", id)
                .body(phoneBookDto)
                .retrieve()
                .body(PhoneBook.class);
    }

    @Override
    public void deletePhoneBook(long id) {
        restClient.delete()
                .uri("/phone_book/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}

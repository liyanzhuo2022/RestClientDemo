package com.restclientdemo.echo_service.clientrest;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.restclientdemo.echo_service.client.PhoneBookClient;
import com.restclientdemo.echo_service.clientrest.configuration.ClientProperties;
import com.restclientdemo.echo_service.domain.PhoneBook;
import com.restclientdemo.echo_service.domain.PhoneBookDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.phone-book-client.type", havingValue = "REST_CLIENT")
public class PhoneBookRestClient implements PhoneBookClient {
    private final RestClient restClient;

    // naive approach: create RestClient in constructor
    PhoneBookRestClient(RestClient.Builder restClientBuilder, ClientProperties properties) {
        restClient = restClientBuilder
                .baseUrl(properties.getBaseHostUrl())
                .build();
    }

    @Override
    public PhoneBook createPhoneBook(PhoneBookDto phoneBookDto) {
        return restClient.post()
                .body(phoneBookDto)
                .retrieve()
                .body(PhoneBook.class);
    }

    @Override
    public List<PhoneBook> getPhoneBooks() {
        return restClient.get()
                .retrieve()
                .body(new ParameterizedTypeReference<List<PhoneBook>>() {});
    }

    @Override
    public PhoneBook updatePhoneBook(long id, PhoneBookDto phoneBookDto) {
        return restClient.put()
                .uri("/{id}", id)
                .body(phoneBookDto)
                .retrieve()
                .body(PhoneBook.class);
    }

    @Override
    public void deletePhoneBook(long id) {
        restClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}

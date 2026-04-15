package com.restclientdemo.echo_service.clientrest;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.restclientdemo.echo_service.client.PhoneBookClient;
import com.restclientdemo.echo_service.clientrest.configuration.PhoneBookClientConfiguration;
import com.restclientdemo.echo_service.domain.PhoneBook;
import com.restclientdemo.echo_service.domain.PhoneBookDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.phone-book-client.type", havingValue = "REST_TEMPLATE")
@Import(PhoneBookClientConfiguration.class)
@RequiredArgsConstructor
public class PhoneBookRestTemplateClient implements PhoneBookClient {
    private final RestTemplate restTemplate;

    @Override
    public PhoneBook createPhoneBook(PhoneBookDto phoneBookDto) {
        var request = new HttpEntity<>(phoneBookDto);

        var response = restTemplate.exchange("/phone_book", POST, request, PhoneBook.class);

        return response.getBody();
    }

    @Override
    public List<PhoneBook> getPhoneBooks() {
        var response = restTemplate.exchange("/phone_book", GET, null, new ParameterizedTypeReference<List<PhoneBook>>() {});

        return response.getBody();
    }

    @Override
    public PhoneBook updatePhoneBook(long id, PhoneBookDto phoneBookDto) {
        var request = new HttpEntity<>(phoneBookDto);

        var response = restTemplate.exchange("/phone_book/{id}", PUT, request, PhoneBook.class, id);
        return response.getBody();
    }

    @Override
    public void deletePhoneBook(long id) {
        restTemplate.exchange("/phone_book/{id}", DELETE, null, void.class, id);
    }
}

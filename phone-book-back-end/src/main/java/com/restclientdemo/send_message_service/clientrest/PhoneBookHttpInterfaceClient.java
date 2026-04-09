package com.restclientdemo.send_message_service.clientrest;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.restclientdemo.send_message_service.client.PhoneBookClient;
import com.restclientdemo.send_message_service.domain.PhoneBook;
import com.restclientdemo.send_message_service.domain.PhoneBookDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.phone-book-client.type", havingValue = "HTTP_CLIENT", matchIfMissing = true)
public class PhoneBookHttpInterfaceClient implements PhoneBookClient {
    private final PhoneBookHttpInterface httpInterface;

    PhoneBookHttpInterfaceClient(RestClient.Builder restClientBuilder) {
        log.info("Creating MessageHttpInterfaceClient");

        var restClient = restClientBuilder
                .baseUrl("http://localhost:8080")
                .build();

        var adapter = RestClientAdapter.create(restClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();
        httpInterface = factory.createClient(PhoneBookHttpInterface.class);
    }

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

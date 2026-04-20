package com.restclientdemo.echo_service.clientrest;

import static org.apache.hc.core5.util.Timeout.ofMilliseconds;

import java.util.List;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.restclientdemo.echo_service.client.PhoneBookClient;
import com.restclientdemo.echo_service.clientrest.configuration.PhoneBookClientProperties;
import com.restclientdemo.echo_service.domain.PhoneBook;
import com.restclientdemo.echo_service.domain.PhoneBookDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.phone-book-client.type", havingValue = "REST_CLIENT")
public class PhoneBookRestClient implements PhoneBookClient {
    private final RestClient restClient;

    PhoneBookRestClient(RestClient.Builder restClientBuilder, PhoneBookClientProperties properties) {
        restClient = restClientBuilder
                .baseUrl(properties.getBaseHostUrl() + "/phone_book")
                .requestFactory(getHttpClientRequestFactory(properties)) // configure request factory
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

    private static ClientHttpRequestFactory getHttpClientRequestFactory(PhoneBookClientProperties properties) {
        int validateAfterInactivityMillis = properties.getValidateAfterInactivityMillis();
        int connectTimeoutMillis = properties.getConnectTimeoutMillis();
        var connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(ofMilliseconds(connectTimeoutMillis))
                .setValidateAfterInactivity(ofMilliseconds(validateAfterInactivityMillis))
                .build();

        var connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(properties.getMaxTotalConnections())
                .setMaxConnPerRoute(properties.getMaxConnectionsPerRoute())
                .setDefaultConnectionConfig(connectionConfig)
                .build();

        var requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(ofMilliseconds(connectTimeoutMillis))
                .setResponseTimeout(ofMilliseconds(properties.getReadTimeoutMillis()))
                .build();

        var httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .disableConnectionState()
                .disableAutomaticRetries()
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}

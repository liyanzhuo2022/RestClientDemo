package com.restclientdemo.send_message_service.clientrest;

import static com.restclientdemo.send_message_service.clientrest.HttpClientFactory.createRestClientInterface;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PhoneBookClientConfiguration {
    String base_url = "http://localhost:8080";

    @Bean("HTTP_CLIENT")
    PhoneBookHttpClient phoneBookHttpClient(RestClient.Builder restClientBuilder) {
        return createRestClientInterface(restClientBuilder, base_url, PhoneBookHttpClient.class);
    }
}

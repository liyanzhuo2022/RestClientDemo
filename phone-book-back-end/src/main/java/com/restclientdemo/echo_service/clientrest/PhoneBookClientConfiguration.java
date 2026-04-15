package com.restclientdemo.echo_service.clientrest;

import static com.restclientdemo.echo_service.clientrest.ClientFactory.createRestClientInterface;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PhoneBookClientConfiguration {

    @Bean("HTTP_CLIENT")
    PhoneBookHttpClient phoneBookHttpClient(RestClient.Builder restClientBuilder, ClientProperties properties) {
        return createRestClientInterface(restClientBuilder, properties, PhoneBookHttpClient.class);
    }

    @Bean
    RestTemplate phoneBookRestTemplate(RestTemplateBuilder restTemplateBuilder, ClientProperties properties) {
        return ClientFactory.createRestTemplate(restTemplateBuilder, properties);
    }
}

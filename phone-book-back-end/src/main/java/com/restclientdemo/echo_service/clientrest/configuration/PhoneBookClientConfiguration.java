package com.restclientdemo.echo_service.clientrest.configuration;

import static com.restclientdemo.echo_service.clientrest.configuration.PhoneBookClientFactory.createRestClientInterface;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import com.restclientdemo.echo_service.clientrest.PhoneBookHttpClient;

@Configuration
public class PhoneBookClientConfiguration {

    @Bean("HTTP_INTERFACE")
    PhoneBookHttpClient phoneBookHttpClient(RestClient.Builder restClientBuilder, PhoneBookClientProperties properties) {
        return createRestClientInterface(restClientBuilder, properties, PhoneBookHttpClient.class);
    }

    @Bean("REST_TEMPLATE")
    RestTemplate phoneBookRestTemplate(RestTemplateBuilder restTemplateBuilder, PhoneBookClientProperties properties) {
        return PhoneBookClientFactory.createRestTemplate(restTemplateBuilder, properties);
    }
}

package com.restclientdemo.echo_service.demo;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

public class UrlDemo {

    static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri("http://httpbin.org")
                .build();

        RestClient restClient = RestClient.builder(restTemplate)
                .baseUrl("http://postman-echo.com")
                .build();

        String body = restClient
                .get()
                .uri("/get")
                .retrieve()
                .body(String.class);

        System.out.println(body);
    }
}

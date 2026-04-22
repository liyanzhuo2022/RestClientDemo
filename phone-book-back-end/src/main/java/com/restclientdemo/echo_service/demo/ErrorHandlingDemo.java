package com.restclientdemo.echo_service.demo;

import java.io.IOException;
import java.net.URI;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.ClientHttpResponse;

public class ErrorHandlingDemo {
    static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {
                return response.getStatusCode().isError();
            }

            @Override
            public void handleError(@NonNull URI url, @NonNull HttpMethod method, @NonNull ClientHttpResponse response) throws IOException {
                System.out.println("RestTemplate error handler triggered");
            }
        });

        RestClient client = RestClient.create(restTemplate);
        String body = client.get()
                .uri("http://httpbin.org/status/404")
                .retrieve()
//                .onStatus(status -> status.value() == 404, (req, res) -> {
//                    System.out.println("RestClient error handler triggered");
//                })
                .body(String.class);

        System.out.println("Done");
    }
}

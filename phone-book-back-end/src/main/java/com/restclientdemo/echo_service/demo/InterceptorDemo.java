package com.restclientdemo.echo_service.demo;

import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

public class InterceptorDemo {
    static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(@NonNull HttpRequest request, byte @NonNull [] body, @NonNull ClientHttpRequestExecution execution) throws IOException {
                System.out.println("RestTemplate interceptor");
                return execution.execute(request, body);
            }
        });

        RestClient client = RestClient.builder(restTemplate)
                .requestInterceptor((req, body, exec) -> {
                    System.out.println("RestClient interceptor");
                    return exec.execute(req, body);
                })
                .build();

        client.get()
                .uri("http://httpbin.org/get")
                .retrieve()
                .body(String.class);

        System.out.println("Done");
    }
}


package com.restclientdemo.send_message_service.clientrest;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class HttpClientFactory {

    public static <T> T createRestClientInterface(RestClient.Builder restClientBuilder, String baseUrl, Class<T> tClass) {
        var restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(tClass);
    }
}

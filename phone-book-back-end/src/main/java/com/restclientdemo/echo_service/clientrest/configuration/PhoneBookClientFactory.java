package com.restclientdemo.echo_service.clientrest.configuration;

import static org.apache.hc.core5.util.Timeout.ofMilliseconds;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;

public class PhoneBookClientFactory {
    public static <T> T createRestClientInterface(RestClient.Builder restClientBuilder, PhoneBookClientProperties properties, Class<T> tClass) {
        var restClient = restClientBuilder
                .baseUrl(properties.baseHostUrl)
                .requestFactory(getHttpClientRequestFactory(properties)) // no need to use lambda for RestClient
                .build();

        var adapter = RestClientAdapter.create(restClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(tClass);
    }

    public static RestTemplate createRestTemplate(RestTemplateBuilder restTemplateBuilder, PhoneBookClientProperties properties) {
        return restTemplateBuilder
                .requestFactory(() -> getHttpClientRequestFactory(properties))
                .rootUri("http://localhost:8080")
                .build();
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

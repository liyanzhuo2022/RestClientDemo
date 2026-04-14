package com.restclientdemo.send_message_service.clientrest;

import static org.apache.hc.core5.util.Timeout.ofMilliseconds;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;

public class HttpClientFactory {
    public static <T> T createRestClientInterface(RestClient.Builder restClientBuilder, ClientProperties properties, Class<T> tClass) {
        var restClient = restClientBuilder
                .baseUrl(properties.baseHostUrl)
                .requestFactory(getHttpClientRequestFactory(properties))
                .build();

        var adapter = RestClientAdapter.create(restClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(tClass);
    }

    private static ClientHttpRequestFactory getHttpClientRequestFactory(ClientProperties properties) {
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

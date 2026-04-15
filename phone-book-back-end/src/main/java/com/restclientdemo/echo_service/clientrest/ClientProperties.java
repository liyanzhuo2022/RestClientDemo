package com.restclientdemo.echo_service.clientrest;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("app.phone-book-client")
public class ClientProperties {
    protected String baseHostUrl;

    protected int maxTotalConnections = 100;

    protected int maxConnectionsPerRoute = 100;

    protected int connectTimeoutMillis = 400;

    protected int readTimeoutMillis = 8000;

    protected int validateAfterInactivityMillis = 1000;
}

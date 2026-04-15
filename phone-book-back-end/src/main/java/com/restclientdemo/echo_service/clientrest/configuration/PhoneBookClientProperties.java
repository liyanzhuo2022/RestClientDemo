package com.restclientdemo.echo_service.clientrest.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("app.phone-book-client")
public class PhoneBookClientProperties {
    protected String baseHostUrl;

    protected PhoneBookClientType type;

    protected int maxTotalConnections = 100;

    protected int maxConnectionsPerRoute = 100;

    protected int connectTimeoutMillis = 400;

    protected int readTimeoutMillis = 8000;

    protected int validateAfterInactivityMillis = 1000;
}

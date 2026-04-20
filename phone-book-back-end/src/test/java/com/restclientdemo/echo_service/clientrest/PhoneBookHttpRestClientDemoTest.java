package com.restclientdemo.echo_service.clientrest;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.wiremock.spring.EnableWireMock;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.restclientdemo.echo_service.clientrest.configuration.PhoneBookClientConfiguration;
import com.restclientdemo.echo_service.clientrest.configuration.PhoneBookClientProperties;
import com.restclientdemo.echo_service.domain.PhoneBook;
import com.restclientdemo.echo_service.domain.PhoneBookDto;

import tools.jackson.databind.json.JsonMapper;

@EnableWireMock
@RestClientTest(value = PhoneBookHttpRestClient.class)
@Import({PhoneBookClientConfiguration.class, PhoneBookClientProperties.class})
@TestPropertySource(properties = {
        "app.phone-book-client.base-host-url=${wiremock.server.baseUrl}",
        "app.phone-book-client.type=HTTP_INTERFACE"
})
public class PhoneBookHttpRestClientDemoTest {

    @Autowired
    private PhoneBookHttpRestClient sut;

    @Autowired
    private JsonMapper mapper;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private RestClient.Builder restClientBuilder;

    @Autowired(required = false)
    private MockRestServiceServer mockServer;

    @Test
    void printHttpInterfacePath() throws Exception {
        System.out.println("\n================ HTTP_INTERFACE DEMO ================");
        System.out.println("sut class = " + sut.getClass().getName());

        System.out.println("MockRestServiceServer bean names = " +
                Arrays.toString(applicationContext.getBeanNamesForType(MockRestServiceServer.class)));
        System.out.println("mockServer bean present = " + (mockServer != null));

        System.out.println("RestClient.Builder bean class = " +
                (restClientBuilder == null ? "null" : restClientBuilder.getClass().getName()));

        dumpBeanNames("org.springframework.web.client.RestClient$Builder");
        dumpBeanNames("org.springframework.boot.restclient.RestTemplateBuilder");

        var dto = newDto();
        var created = phoneBook();

        givenThat(post("/phone_book")
                .withRequestBody(equalToJson(mapper.writeValueAsString(dto)))
                .willReturn(okJson(mapper.writeValueAsString(created))));

        System.out.println("WireMock requests BEFORE business call = " + WireMock.getAllServeEvents().size());

        try {
            var result = sut.createPhoneBook(dto);
            System.out.println("Business call returned = " + mapper.writeValueAsString(result));
        } catch (Exception e) {
            System.out.println("Business call threw: " + e.getClass().getName());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace(System.out);
        }

        var events = WireMock.getAllServeEvents();
        System.out.println("WireMock requests AFTER business call = " + events.size());
        for (int i = 0; i < events.size(); i++) {
            ServeEvent event = events.get(i);
            System.out.println("WireMock event[" + i + "] url=" + event.getRequest().getUrl()
                    + ", method=" + event.getRequest().getMethod()
                    + ", body=" + event.getRequest().getBodyAsString());
        }

        System.out.println("=====================================================\n");
    }

    private void dumpBeanNames(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            System.out.println("Beans of [" + className + "] = " +
                    Arrays.toString(applicationContext.getBeanNamesForType(clazz)));
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot load class: " + className);
        }
    }

    private PhoneBookDto newDto() {
        var dto = new PhoneBookDto();
        dto.setUserName("Alice");
        dto.setPhoneNumber("123456");
        return dto;
    }

    private PhoneBook phoneBook() {
        var pb = new PhoneBook();
        pb.setId(1L);
        pb.setUserName("Alice");
        pb.setPhoneNumber("123456");
        return pb;
    }
}

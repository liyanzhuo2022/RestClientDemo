package com.restclientdemo.echo_service.clientrest;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.wiremock.spring.EnableWireMock;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.restclientdemo.echo_service.clientrest.configuration.PhoneBookClientConfiguration;
import com.restclientdemo.echo_service.clientrest.configuration.PhoneBookClientProperties;
import com.restclientdemo.echo_service.domain.PhoneBook;
import com.restclientdemo.echo_service.domain.PhoneBookDto;

import tools.jackson.databind.json.JsonMapper;

@EnableWireMock
@RestClientTest(value = PhoneBookRestTemplateClient.class)
@Import({PhoneBookClientConfiguration.class})
@EnableConfigurationProperties(PhoneBookClientProperties.class)
@TestPropertySource(properties = {
        "app.phone-book-client.base-host-url=${wiremock.server.baseUrl}",
        "app.phone-book-client.type=REST_TEMPLATE"
})
public class PhoneBookHttpRestTemplateDemoTest {

    @Autowired
    private JsonMapper mapper;

    @Autowired
    private PhoneBookRestTemplateClient sut;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Autowired(required = false)
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired(required = false)
    private MockRestServiceServer mockServer;

    @Test
    void printRestTemplatePath() throws Exception {
        System.out.println("\n================ REST_TEMPLATE DEMO ================");
        System.out.println("sut class = " + sut.getClass().getName());

        System.out.println("MockRestServiceServer bean names = " +
                Arrays.toString(applicationContext.getBeanNamesForType(MockRestServiceServer.class)));
        System.out.println("mockServer bean present = " + (mockServer != null));

        System.out.println("RestTemplateBuilder bean class = " +
                (restTemplateBuilder == null ? "null" : restTemplateBuilder.getClass().getName()));

        System.out.println("RestTemplate bean class = " +
                (restTemplate == null ? "null" : restTemplate.getClass().getName()));

        if (restTemplate != null) {
            System.out.println("RestTemplate requestFactory class = " +
                    restTemplate.getRequestFactory().getClass().getName());
        }

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

        System.out.println("====================================================\n");
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
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


/**================ REST_TEMPLATE DEMO ================
 sut class = com.restclientdemo.echo_service.clientrest.PhoneBookRestTemplateClient
 MockRestServiceServer bean names = [mockRestServiceServer]
 mockServer bean present = true
 RestTemplateBuilder bean class = org.springframework.boot.restclient.RestTemplateBuilder
 RestTemplate bean class = org.springframework.web.client.RestTemplate
 RestTemplate requestFactory class = org.springframework.test.web.client.MockRestServiceServer$MockClientHttpRequestFactory
 2026-04-20T14:52:03.043+02:00  INFO 30465 --- [echo-service] [tp1828438007-54] o.e.j.s.handler.ContextHandler.___admin  : RequestHandlerClass from context returned com.github.tomakehurst.wiremock.http.AdminRequestHandler. Normalized mapped under returned 'null'
 2026-04-20T14:52:03.049+02:00  INFO 30465 --- [echo-service] [tp1828438007-54] WireMock.wiremock                        : Admin request received:
 127.0.0.1 - POST /mappings

 Content-Type: [application/json]
 Host: [localhost:54662]
 Content-Length: [467]
 Connection: [keep-alive]
 User-Agent: [Apache-HttpClient/5.5.2 (Java/25.0.2)]
 {
 "id" : "84a0c540-41a9-4bc5-8489-c9c66cab4581",
 "request" : {
 "url" : "/phone_book",
 "method" : "POST",
 "bodyPatterns" : [ {
 "equalToJson" : "{\"phoneNumber\":\"123456\",\"userName\":\"Alice\"}"
 } ]
 },
 "response" : {
 "status" : 200,
 "body" : "{\"id\":1,\"phoneNumber\":\"123456\",\"userName\":\"Alice\"}",
 "headers" : {
 "Content-Type" : "application/json"
 }
 },
 "uuid" : "84a0c540-41a9-4bc5-8489-c9c66cab4581"
 }

 2026-04-20T14:52:03.079+02:00  INFO 30465 --- [echo-service] [tp1828438007-48] WireMock.wiremock                        : Admin request received:
 127.0.0.1 - GET /requests

 Host: [localhost:54662]
 Connection: [keep-alive]
 User-Agent: [Apache-HttpClient/5.5.2 (Java/25.0.2)]


 WireMock requests BEFORE business call = 0

 java.lang.AssertionError: No further requests expected: HTTP POST /phone_book
 0 request(s) executed.


 at org.springframework.test.web.client.AbstractRequestExpectationManager.createUnexpectedRequestError(AbstractRequestExpectationManager.java:194)
 at org.springframework.test.web.client.SimpleRequestExpectationManager.matchRequest(SimpleRequestExpectationManager.java:59)
 at org.springframework.test.web.client.AbstractRequestExpectationManager.validateRequest(AbstractRequestExpectationManager.java:92)
 at org.springframework.boot.restclient.test.RootUriRequestExpectationManager.validateRequest(RootUriRequestExpectationManager.java:82)
 at org.springframework.test.web.client.MockRestServiceServer$MockClientHttpRequestFactory$1.executeInternal(MockRestServiceServer.java:331)
 at org.springframework.mock.http.client.MockClientHttpRequest.execute(MockClientHttpRequest.java:140)
 at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:754)
 * */
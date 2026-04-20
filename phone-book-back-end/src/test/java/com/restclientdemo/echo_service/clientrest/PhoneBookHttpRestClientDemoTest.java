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

/**
 * ================ HTTP_INTERFACE DEMO ================
 * sut class = com.restclientdemo.echo_service.clientrest.PhoneBookHttpRestClient
 * MockRestServiceServer bean names = [mockRestServiceServer]
 * mockServer bean present = true
 * RestClient.Builder bean class = org.springframework.web.client.DefaultRestClientBuilder
 * Beans of [org.springframework.web.client.RestClient$Builder] = [restClientBuilder]
 * Beans of [org.springframework.boot.restclient.RestTemplateBuilder] = [restTemplateBuilder]
 * 2026-04-20T14:53:47.857+02:00  INFO 30512 --- [echo-service] [tp1644128841-54] o.e.j.s.handler.ContextHandler.___admin  : RequestHandlerClass from context returned com.github.tomakehurst.wiremock.http.AdminRequestHandler. Normalized mapped under returned 'null'
 * 2026-04-20T14:53:47.862+02:00  INFO 30512 --- [echo-service] [tp1644128841-54] WireMock.wiremock                        : Admin request received:
 * 127.0.0.1 - POST /mappings
 *
 * Content-Type: [application/json]
 * Host: [localhost:54802]
 * Content-Length: [467]
 * Connection: [keep-alive]
 * User-Agent: [Apache-HttpClient/5.5.2 (Java/25.0.2)]
 * {
 *   "id" : "81c51fb1-3198-4e31-bc3c-8497270cbd95",
 *   "request" : {
 *     "url" : "/phone_book",
 *     "method" : "POST",
 *     "bodyPatterns" : [ {
 *       "equalToJson" : "{\"phoneNumber\":\"123456\",\"userName\":\"Alice\"}"
 *     } ]
 *   },
 *   "response" : {
 *     "status" : 200,
 *     "body" : "{\"id\":1,\"phoneNumber\":\"123456\",\"userName\":\"Alice\"}",
 *     "headers" : {
 *       "Content-Type" : "application/json"
 *     }
 *   },
 *   "uuid" : "81c51fb1-3198-4e31-bc3c-8497270cbd95"
 * }
 *
 * 2026-04-20T14:53:47.892+02:00  INFO 30512 --- [echo-service] [tp1644128841-48] WireMock.wiremock                        : Admin request received:
 * 127.0.0.1 - GET /requests
 *
 * Host: [localhost:54802]
 * Connection: [keep-alive]
 * User-Agent: [Apache-HttpClient/5.5.2 (Java/25.0.2)]
 *
 *
 * WireMock requests BEFORE business call = 0
 * 2026-04-20T14:53:47.925+02:00  INFO 30512 --- [echo-service] [tp1644128841-48] WireMock.wiremock                        : Request received:
 * 127.0.0.1 - POST /phone_book
 *
 * Content-Type: [application/json]
 * Accept-Encoding: [gzip, x-gzip, deflate]
 * Host: [localhost:54802]
 * Transfer-Encoding: [chunked]
 * Connection: [keep-alive]
 * User-Agent: [Apache-HttpClient/5.5.2 (Java/25.0.2)]
 * {"phoneNumber":"123456","userName":"Alice"}
 *
 *
 * Matched response definition:
 * {
 *   "status" : 200,
 *   "body" : "{\"id\":1,\"phoneNumber\":\"123456\",\"userName\":\"Alice\"}",
 *   "headers" : {
 *     "Content-Type" : "application/json"
 *   }
 * }
 *
 * Response:
 * HTTP/1.1 200
 * Content-Type: [application/json]
 * Matched-Stub-Id: [81c51fb1-3198-4e31-bc3c-8497270cbd95]
 *
 * Business call returned = {"id":1,"phoneNumber":"123456","userName":"Alice"}
 * 2026-04-20T14:53:47.941+02:00  INFO 30512 --- [echo-service] [tp1644128841-53] WireMock.wiremock                        : Admin request received:
 * 127.0.0.1 - GET /requests
 *
 * Host: [localhost:54802]
 * Connection: [keep-alive]
 * User-Agent: [Apache-HttpClient/5.5.2 (Java/25.0.2)]
 *
 *
 * WireMock requests AFTER business call = 1
 * WireMock event[0] url=/phone_book, method=POST, body={"phoneNumber":"123456","userName":"Alice"}
 * =====================================================
 *
 * 2026-04-20T14:53:47.958+02:00  INFO 30512 --- [echo-service] [ionShutdownHook] c.w.s.i.WireMockServerCreator wiremock   : Stopping WireMockServer with name 'wiremock'
 * 2026-04-20T14:53:47.958+02:00  INFO 30512 --- [echo-service] [ionShutdownHook] org.eclipse.jetty.server.Server          : Stopped oejs.Server@61b65d54{STOPPING}[12.1.7,sto=1000]
 * 2026-04-20T14:53:47.958+02:00  INFO 30512 --- [echo-service] [ionShutdownHook] org.eclipse.jetty.server.Server          : Shutdown oejs.Server@61b65d54{STOPPING}[12.1.7,sto=1000]
 * 2026-04-20T14:53:47.960+02:00  INFO 30512 --- [echo-service] [ionShutdownHook] o.e.jetty.server.AbstractConnector       : Stopped oejs.NetworkTrafficServerConnector@402f8592{HTTP/1.1, (http/1.1, h2c)}{0.0.0.0:0}
 * 2026-04-20T14:53:47.961+02:00  INFO 30512 --- [echo-service] [ionShutdownHook] o.e.j.e.servlet.ServletContextHandler    : Stopped oeje10s.ServletContextHandler@38a4e2b0{ROOT,/,b=null,a=AVAILABLE,h=oeje10s.ServletHandler@14c99bf6{STOPPED}}
 * 2026-04-20T14:53:47.961+02:00  INFO 30512 --- [echo-service] [ionShutdownHook] o.e.j.e.servlet.ServletContextHandler    : Stopped oeje10s.ServletContextHandler@4962b41e{/__admin,/__admin,b=null,a=AVAILABLE,h=oeje10s.ServletHandler@4fecf308{STOPPED}}
 *
 * Process finished with exit code 0
 * */
package com.restclientdemo.echo_service.clientrest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.wiremock.spring.EnableWireMock;

import com.restclientdemo.echo_service.clientrest.configuration.ClientProperties;
import com.restclientdemo.echo_service.clientrest.configuration.PhoneBookClientConfiguration;
import com.restclientdemo.echo_service.domain.PhoneBook;
import com.restclientdemo.echo_service.domain.PhoneBookDto;

import tools.jackson.databind.json.JsonMapper;

@EnableWireMock
@RestClientTest(value = PhoneBookHttpRestClient.class, properties = "app.phone-book-client.type=HTTP_INTERFACE")
@Import({PhoneBookClientConfiguration.class, ClientProperties.class})
@TestPropertySource(properties = {"app.phone-book-client.base-host-url=${wiremock.server.baseUrl}/phone_book"})
class PhoneBookHttpRestClientTest {
    @Autowired
    private PhoneBookHttpRestClient sut;

    @Autowired
    private JsonMapper mapper;

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

    @Test
    void createPhoneBook() {
        var dto = newDto();
        var created = phoneBook();

        givenThat(post("/phone_book")
                .withRequestBody(equalToJson(mapper.writeValueAsString(dto)))
                .willReturn(okJson(mapper.writeValueAsString(created))));

        var result = sut.createPhoneBook(dto);

        assertThat(result.getId()).isEqualTo(created.getId());
        assertThat(result.getUserName()).isEqualTo(created.getUserName());
        assertThat(result.getPhoneNumber()).isEqualTo(created.getPhoneNumber());
    }

    @Test
    void getPhoneBooks() {
        var list = List.of(phoneBook());

        givenThat(get("/phone_book")
                .willReturn(okJson(mapper.writeValueAsString(list))));

        var result = sut.getPhoneBooks();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getUserName()).isEqualTo("Alice");
    }

    @Test
    void updatePhoneBook() {
        var dto = newDto();
        var updated = phoneBook();
        updated.setUserName("UpdatedName");

        givenThat(put("/phone_book/1")
                .withRequestBody(equalToJson(mapper.writeValueAsString(dto)))
                .willReturn(okJson(mapper.writeValueAsString(updated))));

        var result = sut.updatePhoneBook(1L, dto);

        assertThat(result.getUserName()).isEqualTo("UpdatedName");
    }

    @Test
    void deletePhoneBook() throws Exception {
        givenThat(delete("/phone_book/1")
                .willReturn(aResponse().withStatus(204)));

        sut.deletePhoneBook(1L);
    }
}
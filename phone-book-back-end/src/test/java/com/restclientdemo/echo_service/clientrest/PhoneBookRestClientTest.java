package com.restclientdemo.echo_service.clientrest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import jakarta.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import com.restclientdemo.echo_service.clientrest.configuration.PhoneBookClientProperties;
import com.restclientdemo.echo_service.domain.PhoneBook;
import com.restclientdemo.echo_service.domain.PhoneBookDto;

import tools.jackson.databind.json.JsonMapper;

@RestClientTest(value = PhoneBookRestClient.class, properties = "app.phone-book-client.type=REST_CLIENT")
@EnableConfigurationProperties(PhoneBookClientProperties.class)
class PhoneBookRestClientTest {

    @Autowired
    private PhoneBookRestClient sut;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private JsonMapper mapper;

    @Autowired
    private PhoneBookClientProperties properties;

    private String url;

    @PostConstruct
    void init() {
        url = properties.getBaseHostUrl() + "/phone_book";
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

    @Test
    void createPhoneBook() {
        var dto = newDto();
        var created = phoneBook();

        server.expect(once(), requestTo(url))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andExpect(content().json(mapper.writeValueAsString(dto)))
                .andRespond(withSuccess(
                        mapper.writeValueAsString(created),
                        MediaType.APPLICATION_JSON
                ));

        var result = sut.createPhoneBook(dto);

        assertThat(result.getId()).isEqualTo(created.getId());
        assertThat(result.getUserName()).isEqualTo(created.getUserName());
        assertThat(result.getPhoneNumber()).isEqualTo(created.getPhoneNumber());
    }

    @Test
    void getPhoneBooks() {
        var list = List.of(phoneBook());

        server.expect(once(), requestTo(url))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(
                        mapper.writeValueAsString(list),
                        MediaType.APPLICATION_JSON
                ));

        var result = sut.getPhoneBooks();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getUserName()).isEqualTo("Alice");
    }

    @Test
    void updatePhoneBook() {
        var dto = newDto();
        var updated = phoneBook();
        updated.setUserName("UpdatedName");

        server.expect(once(), requestTo(url + "/1"))
                .andExpect(method(org.springframework.http.HttpMethod.PUT))
                .andExpect(content().json(mapper.writeValueAsString(dto)))
                .andRespond(withSuccess(
                        mapper.writeValueAsString(updated),
                        MediaType.APPLICATION_JSON
                ));

        var result = sut.updatePhoneBook(1L, dto);

        assertThat(result.getUserName()).isEqualTo("UpdatedName");
    }

    @Test
    void deletePhoneBook() throws Exception {
        server.expect(once(), requestTo(url + "/1"))
                .andExpect(method(org.springframework.http.HttpMethod.DELETE))
                .andRespond(withNoContent());

        sut.deletePhoneBook(1L);

        server.verify();
    }
}
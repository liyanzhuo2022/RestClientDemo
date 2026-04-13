package com.restclientdemo.send_message_service.clientrest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import com.restclientdemo.send_message_service.domain.PhoneBook;
import com.restclientdemo.send_message_service.domain.PhoneBookDto;

import tools.jackson.databind.ObjectMapper;

@RestClientTest(value = PhoneBookRestClient.class, properties = "app.phone-book-client.type=REST_CLIENT")
class PhoneBookRestClientTest {

    @Autowired
    private PhoneBookRestClient sut;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

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

        server.expect(once(), requestTo("http://localhost:8080/phone_book"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andExpect(content().json(objectMapper.writeValueAsString(dto)))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(created),
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

        server.expect(once(), requestTo("http://localhost:8080/phone_book"))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(list),
                        MediaType.APPLICATION_JSON
                ));

        var result = sut.getPhoneBooks();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("Alice");
    }

    @Test
    void updatePhoneBook() {
        var dto = newDto();
        var updated = phoneBook();
        updated.setUserName("UpdatedName");

        server.expect(once(), requestTo("http://localhost:8080/phone_book/1"))
                .andExpect(method(org.springframework.http.HttpMethod.PUT))
                .andExpect(content().json(objectMapper.writeValueAsString(dto)))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(updated),
                        MediaType.APPLICATION_JSON
                ));

        var result = sut.updatePhoneBook(1L, dto);

        assertThat(result.getUserName()).isEqualTo("UpdatedName");
    }

    @Test
    void deletePhoneBook() throws Exception {
        server.expect(once(), requestTo("http://localhost:8080/phone_book/1"))
                .andExpect(method(org.springframework.http.HttpMethod.DELETE))
                .andRespond(withNoContent());

        sut.deletePhoneBook(1L);

        server.verify();
    }
}
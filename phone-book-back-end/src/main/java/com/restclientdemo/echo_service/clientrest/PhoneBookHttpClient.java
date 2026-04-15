package com.restclientdemo.echo_service.clientrest;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.restclientdemo.echo_service.domain.PhoneBook;
import com.restclientdemo.echo_service.domain.PhoneBookDto;

@HttpExchange("/phone_book")
public interface PhoneBookHttpClient {

    @PostExchange
    PhoneBook createPhoneBook(@RequestBody PhoneBookDto phoneBookDto);

    @GetExchange
    List<PhoneBook> getPhoneBooks();

    @PutExchange("/{id}")
    PhoneBook updatePhoneBook(@PathVariable long id, @RequestBody PhoneBookDto phoneBookDto);

    @DeleteExchange("/{id}")
    void deletePhoneBook(@PathVariable long id);
}

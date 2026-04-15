package com.restclientdemo.echo_service.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restclientdemo.echo_service.core.PhoneBookService;
import com.restclientdemo.echo_service.domain.PhoneBook;
import com.restclientdemo.echo_service.domain.PhoneBookDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/phone_book")
@RequiredArgsConstructor
public class PhoneBookRestController {
    private final PhoneBookService phoneBookService;

    @PostMapping
    public PhoneBook createPhoneBook(@RequestBody PhoneBookDto phoneBookDto) {
        return phoneBookService.createPhoneBook(phoneBookDto);
    }

    @GetMapping
    public List<PhoneBook> getPhoneBooks() {
        return phoneBookService.getPhoneBooks();
    }

    @PutMapping("/{id}")
    public PhoneBook updatePhoneBook(@PathVariable long id, @RequestBody PhoneBookDto phoneBookDto) {
        return phoneBookService.updatePhoneBook(id, phoneBookDto);
    }

    @DeleteMapping("/{id}")
    public void deletePhoneBook(@PathVariable long id) {
        phoneBookService.deletePhoneBook(id);
    }
}

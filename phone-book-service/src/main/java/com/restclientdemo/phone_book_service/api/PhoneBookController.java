package com.restclientdemo.phone_book_service.api;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restclientdemo.phone_book_service.entity.PhoneBook;
import com.restclientdemo.phone_book_service.api.model.PhoneBookDto;
import com.restclientdemo.phone_book_service.core.PhoneBookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("phone_book")
@RequiredArgsConstructor
public class PhoneBookController {
    private final PhoneBookService phoneBookService;

    @PostMapping
    public ResponseEntity<PhoneBook> createPhoneBook(@RequestBody PhoneBookDto phoneBookDto) {
        PhoneBook phoneBook = phoneBookService.createPhoneBook(phoneBookDto);
        return ok(phoneBook);
    }

    @GetMapping
    public ResponseEntity<List<PhoneBook>> getPhoneBooks() {
        List<PhoneBook> phoneBooks = phoneBookService.getPhoneBooks();
        return ok(phoneBooks);
    }

    @PutMapping("{id}")
    public ResponseEntity<PhoneBook> updatePhoneBook(@PathVariable long id, @RequestBody PhoneBookDto phoneBookDto) {
        try {
            return ok(phoneBookService.updatePhoneBook(id, phoneBookDto));
        } catch (RuntimeException e) {
            return notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePhoneBook(@PathVariable long id) {
        try {
            phoneBookService.deletePhoneBook(id);
            return ok().build();
        } catch (RuntimeException e) {
            return notFound().build();
        }
    }
}

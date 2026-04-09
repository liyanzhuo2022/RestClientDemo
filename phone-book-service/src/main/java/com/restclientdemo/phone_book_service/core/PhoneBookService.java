package com.restclientdemo.phone_book_service.core;

import java.util.List;

import org.springframework.stereotype.Service;

import com.restclientdemo.phone_book_service.entity.PhoneBook;
import com.restclientdemo.phone_book_service.api.model.PhoneBookDto;
import com.restclientdemo.phone_book_service.mapper.PhoneBookMapper;
import com.restclientdemo.phone_book_service.repository.PhoneBookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhoneBookService {
    private final PhoneBookRepository repository;
    private final PhoneBookMapper mapper;

    public PhoneBook createPhoneBook(PhoneBookDto phoneBookDto) {
        PhoneBook phoneBook = mapper.dtoToPhoneBook(phoneBookDto);
        return repository.save(phoneBook);
    }

    public List<PhoneBook> getPhoneBooks() {
        return repository.findAll();
    }

    public PhoneBook updatePhoneBook(long id, PhoneBookDto phoneBookDto) {
        PhoneBook phoneBook = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PhoneBook not found"));

        phoneBook.setUserName(phoneBookDto.getUserName());
        phoneBook.setPhoneNumber(phoneBookDto.getPhoneNumber());

        return repository.save(phoneBook);
    }

    public void deletePhoneBook(long id) {
        PhoneBook phoneBook = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PhoneBook not found"));
        repository.deleteById(id);
    }


}

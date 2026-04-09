package com.restclientdemo.phone_book_service.mapper;

import org.mapstruct.Mapper;

import com.restclientdemo.phone_book_service.entity.PhoneBook;
import com.restclientdemo.phone_book_service.api.model.PhoneBookDto;

@Mapper(componentModel = "spring")
public interface PhoneBookMapper {
    PhoneBook dtoToPhoneBook(PhoneBookDto phoneBookDto);
    PhoneBookDto phoneBookToDto(PhoneBook phoneBook);
}

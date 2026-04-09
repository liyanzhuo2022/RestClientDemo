package com.restclientdemo.phone_book_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restclientdemo.phone_book_service.entity.PhoneBook;

@Repository
public interface PhoneBookRepository extends JpaRepository<PhoneBook, Long> {
    List<PhoneBook> getPhoneBookById(long id);
}
package com.fakerestapi.test.utils;

import com.fakerestapi.test.models.Book;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BooksHelper {

    private final Faker faker = new Faker();

    public Book createBookWithFakeData() {
        return Book.builder()
                .title(faker.book().title())
                .description(faker.lorem().paragraph())
                .pageCount(faker.number().numberBetween(1, 1000))
                .excerpt(faker.lorem().paragraph())
                .publishDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .build();
    }

    public Book createBookWithEmptyData() {
        return Book.builder()
                .id(0)
                .title(null)
                .description(null)
                .pageCount(0)
                .excerpt(null)
                .publishDate(null)
                .build();
    }

}

package com.fakerestapi.test.utils;

import com.fakerestapi.test.models.Author;
import com.github.javafaker.Faker;

public class AuthorHelper {

    private static final Faker faker = new Faker();

    public Author createAuthorWithFakeData() {
        return Author.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .idBook(faker.number().numberBetween(1, 1000))
                .build();
    }

    public Author createAuthorEmptyData() {
        return Author.builder()
                .id(0)
                .idBook(0)
                .firstName(null)
                .lastName(null)
                .build();
    }
}

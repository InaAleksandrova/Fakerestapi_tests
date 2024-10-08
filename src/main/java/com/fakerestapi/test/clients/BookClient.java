package com.fakerestapi.test.clients;

import com.fakerestapi.test.config.ApiConfig;
import com.fakerestapi.test.models.Book;
import io.restassured.response.Response;

public class BookClient extends BaseClient {

    public Response getAllBooks() {
        return getAll(ApiConfig.BOOKS);
    }

    public Response getBookById(int id) {
        return getWithPathParam(ApiConfig.BOOK_BY_ID, "id", id);
    }

    public Response addBook(Book book) {
        return post(ApiConfig.BOOKS, book);
    }

    public Response updateBook(int id, Book book) {
        return putWithPathParam(ApiConfig.BOOK_BY_ID, "id", id, book);
    }

    public Response deleteBook(int id) {
        return deleteWithPathParam(ApiConfig.BOOK_BY_ID, "id", id);
    }
}

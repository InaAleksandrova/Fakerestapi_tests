package com.fakerestapi.test.clients;

import com.fakerestapi.test.config.ApiConfig;
import io.restassured.response.Response;

public class BookClient extends BaseClient {

    public Response getAllBooks() {
        return getAll(ApiConfig.BOOKS);
    }

    public Response getBookById(int id) {
        return getWithPathParam(ApiConfig.BOOK_BY_ID, "id", id);
    }

    public Response addBook(String bookJson) {
        return post(ApiConfig.BOOKS, bookJson);
    }

    public Response updateBook(Object id, String bookJson) {
        return putWithPathParam(ApiConfig.BOOK_BY_ID, "id", id, bookJson);
    }

    public Response deleteBook(Object id) {
        return deleteWithPathParam(ApiConfig.BOOK_BY_ID, "id", id);
    }
}

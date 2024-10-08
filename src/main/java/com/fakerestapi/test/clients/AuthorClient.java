package com.fakerestapi.test.clients;

import com.fakerestapi.test.config.ApiConfig;
import com.fakerestapi.test.models.Author;
import io.restassured.response.Response;

public class AuthorClient extends BaseClient {

    public Response getAllAuthors() {
        return getAll(ApiConfig.AUTHORS);
    }

    public Response getAuthorById(int id) {
        return getWithPathParam(ApiConfig.AUTHOR_BY_ID, "id", id);
    }

    public Response addAuthor(Author author) {
        return post(ApiConfig.AUTHORS, author);
    }

    public Response updateAuthor(int id, Author author) {
        return putWithPathParam(ApiConfig.AUTHOR_BY_ID, "id", id, author);
    }

    public Response deleteAuthor(int id) {
        return deleteWithPathParam(ApiConfig.AUTHOR_BY_ID, "id", id);
    }
}

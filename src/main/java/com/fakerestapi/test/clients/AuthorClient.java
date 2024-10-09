package com.fakerestapi.test.clients;

import com.fakerestapi.test.config.ApiConfig;
import com.fakerestapi.test.models.Author;
import io.restassured.response.Response;

public class AuthorClient extends BaseClient {

    public Response getAllAuthors() {
        return getAll(ApiConfig.AUTHORS);
    }

    public Response getAuthorById(Object id) {
        return getWithPathParam(ApiConfig.AUTHOR_BY_ID, "id", id);
    }

    public Response addAuthor(String authorJson) {
        return post(ApiConfig.AUTHORS, authorJson);
    }

    public Response updateAuthor(int id, String authorJson) {
        return putWithPathParam(ApiConfig.AUTHOR_BY_ID, "id", id, authorJson);
    }

    public Response deleteAuthor(int id) {
        return deleteWithPathParam(ApiConfig.AUTHOR_BY_ID, "id", id);
    }
}

package com.fakerestapi.test;

import com.fakerestapi.test.clients.AuthorClient;
import com.fakerestapi.test.models.Author;
import com.fakerestapi.test.utils.AuthorHelper;
import com.fakerestapi.test.validators.ResponseValidator;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

public class AuthorsTests extends BaseTests {

    private AuthorClient authorClient;
    private AuthorHelper authorHelper;
    private Gson gson;

    @BeforeClass
    public void setUpAuthorsTests() {
        authorClient = new AuthorClient();
        authorHelper = new AuthorHelper();
        gson = new Gson();
    }

    @Test(description = "Verify a list of all authors is displayed")
    public void getAllAuthorsTest() {
        Response response = authorClient.getAllAuthors();
        ResponseValidator.validateStatusCode(response, HttpStatus.SC_OK);
        ResponseValidator.validateListOfIdsNotEmpty(response);
    }

    @Test(description = "Verify an author can be retrieved by a valid id")
    public void getAuthorByIdTest() {
        Response allAuthorsResponse = authorClient.getAllAuthors();
        ResponseValidator.validateStatusCode(allAuthorsResponse, HttpStatus.SC_OK);
        ResponseValidator.validateListOfIdsNotEmpty(allAuthorsResponse);

        int randomId = authorHelper.getRandomAuthorId();

        Response response = authorClient.getAuthorById(randomId);
        ResponseValidator.validateStatusCode(allAuthorsResponse, 200);

        Author retrievedAuthor = response.as(Author.class);

        Assert.assertEquals(retrievedAuthor.getId(), randomId);
        Assert.assertNotNull(retrievedAuthor.getFirstName(), "First name field should not be null");
        Assert.assertNotNull(retrievedAuthor.getLastName(), "Last name field should not be null");
    }

    @Test(description = "Verify that an author cannot be retrieved using an id bigger than the authors list size")
    public void getAuthorByOutOfBoundsId() {
        List<Integer> authors = authorClient.getAllAuthors().jsonPath().getList("id", Integer.class);
        int invalidAuthorId = authors.size() + 1;

        Response response = authorClient.getAuthorById(invalidAuthorId);
        ResponseValidator.validateStatusCode(response, HttpStatus.SC_NOT_FOUND);
        //add validation for error message text
    }

    @Test(description = "Add a new author to authors repository")
    public void addNewAuthorTest() {;
        Author newAuthor = authorHelper.createAuthorWithFakeData();
        String newAuthorJson = gson.toJson(newAuthor);

        Response postResponse = authorClient.addAuthor(newAuthorJson).then().extract().response();

        ResponseValidator.validateStatusCode(postResponse, 200);
        assertNotNull(postResponse.jsonPath().getString("id"), "The author ID should not be empty");

        Author createdAuthor = postResponse.as(Author.class);

        Assert.assertEquals(createdAuthor.getId(), newAuthor.getId());
        Assert.assertEquals(createdAuthor.getIdBook(), newAuthor.getIdBook());
        Assert.assertEquals(createdAuthor.getFirstName(), newAuthor.getFirstName());
        Assert.assertEquals(createdAuthor.getLastName(), newAuthor.getLastName());
    }

    @Test(description = "Update an existing author data")
    public void updateAuthorById() {
        Response getAllAuthorsResponse = authorClient.getAllAuthors();
        ResponseValidator.validateStatusCode(getAllAuthorsResponse, 200);

        int id = authorHelper.getRandomAuthorId();

        Response authorForUpdate = authorClient.getAuthorById(id);
        Author beforeUpdateAuthor = authorForUpdate.as(Author.class);

        Author author = authorHelper.createAuthorWithFakeData();
        String authorJson = gson.toJson(author);

        Response updateAuthorResponse = authorClient.updateAuthor(id, authorJson);

        Author updatedAuthor = updateAuthorResponse.as(Author.class);

        ResponseValidator.validateStatusCode(updateAuthorResponse, 200);
        assertNotEquals(updatedAuthor.getId(), beforeUpdateAuthor.getId());
        assertNotEquals(updatedAuthor.getIdBook(), beforeUpdateAuthor.getIdBook());
        assertNotEquals(updatedAuthor.getFirstName(), beforeUpdateAuthor.getFirstName());
        assertNotEquals(updatedAuthor.getLastName(), beforeUpdateAuthor.getLastName());
    }

    @Test(description = "Delete an author")
    public void deleteAuthorById() {
        int id = authorHelper.getRandomAuthorId();
        authorClient.deleteAuthor(id).then().statusCode(200);
        authorClient.deleteAuthor(id).then().statusCode(404);
    }


}

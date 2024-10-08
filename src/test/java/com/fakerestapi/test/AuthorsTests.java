package com.fakerestapi.test;

import com.fakerestapi.test.clients.AuthorClient;
import com.fakerestapi.test.models.Author;
import com.fakerestapi.test.utils.AuthorHelper;
import com.fakerestapi.test.validators.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

public class AuthorsTests extends BaseTests {

    private AuthorClient authorClient;
    private AuthorHelper authorsFakerUtil;
    private Random random;

    @BeforeClass
    public void setUpAuthorsTests() {
        authorClient = new AuthorClient();
        authorsFakerUtil = new AuthorHelper();
        random = new Random();
    }

    @Test(description = "Get a list of all authors", priority = 1)
    public void getAllAuthorsTest() {
        Response response = authorClient.getAllAuthors();
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateListOfIdsNotEmpty(response);
    }

    @Test(description = "Get an author by a valid ID", priority = 2)
    public void getAuthorByIdTest() {
        Response allAuthorsResponse = authorClient.getAllAuthors();
        ResponseValidator.validateStatusCode(allAuthorsResponse, 200);
        ResponseValidator.validateListOfIdsNotEmpty(allAuthorsResponse);

        int randomId = random.nextInt(authorClient.getAllAuthors().jsonPath().getList("id", Integer.class).size());

        Response response = authorClient.getAuthorById(randomId);
        ResponseValidator.validateStatusCode(allAuthorsResponse, 200);

        Author retrievedAuthor = response.as(Author.class);

        Assert.assertEquals(retrievedAuthor.getId(), randomId);
        Assert.assertNotNull(retrievedAuthor.getFirstName(), "First name field should not be null");
        Assert.assertNotNull(retrievedAuthor.getLastName(), "Last name field should not be null");
    }

    @Test(description = "Retrieve an author by invalid ID", priority = 3)
    public void getAuthorByInvalidId() {

        List<Integer> authors = authorClient.getAllAuthors().jsonPath().getList("id", Integer.class);
        int invalidAuthorId = authors.size() + 1;
        Response response = authorClient.getAuthorById(invalidAuthorId);
        ResponseValidator.validateStatusCode(response, 404);
        //add validation for error message text
    }

    @Test(description = "Add a new author to authors repository", priority = 4)
    public void addNewAuthorTest() {;
        Author newAuthor = authorsFakerUtil.createAuthorWithFakeData();
        Response postResponse = authorClient.addAuthor(newAuthor).then().extract().response();

        ResponseValidator.validateStatusCode(postResponse, 200);
        assertNotNull(postResponse.jsonPath().getString("id"), "The author ID should not be empty");

        Author createdAuthor = postResponse.as(Author.class);

        Assert.assertEquals(createdAuthor.getId(), newAuthor.getId());
        Assert.assertEquals(createdAuthor.getIdBook(), newAuthor.getIdBook());
        Assert.assertEquals(createdAuthor.getFirstName(), newAuthor.getFirstName());
        Assert.assertEquals(createdAuthor.getLastName(), newAuthor.getLastName());
    }

    @Test(description = "Update an existing author data", priority = 5)
    public void updateAuthorById() {
        Response getAllAuthorsResponse = authorClient.getAllAuthors();
        ResponseValidator.validateStatusCode(getAllAuthorsResponse, 200);

        int id = random.nextInt(authorClient.getAllAuthors().jsonPath().getList("id", Integer.class).size());

        Response authorForUpdate = authorClient.getAuthorById(id);
        Author beforeUpdateAuthor = authorForUpdate.as(Author.class);

        Author author = authorsFakerUtil.createAuthorWithFakeData();

        Response updateAuthorResponse = authorClient.updateAuthor(id, author);

        Author updatedAuthor = updateAuthorResponse.as(Author.class);

        ResponseValidator.validateStatusCode(updateAuthorResponse, 200);
        assertNotEquals(updatedAuthor.getId(), beforeUpdateAuthor.getId());
        assertNotEquals(updatedAuthor.getIdBook(), beforeUpdateAuthor.getIdBook());
        assertNotEquals(updatedAuthor.getFirstName(), beforeUpdateAuthor.getFirstName());
        assertNotEquals(updatedAuthor.getLastName(), beforeUpdateAuthor.getLastName());
    }

    @Test(description = "Delete an author", priority = 6)
    public void deleteAuthorById() {
        int id = random.nextInt(authorClient.getAllAuthors().jsonPath().getList("id", Integer.class).size());
        authorClient.deleteAuthor(id).then().statusCode(200);
        authorClient.deleteAuthor(id).then().statusCode(404);
    }


}

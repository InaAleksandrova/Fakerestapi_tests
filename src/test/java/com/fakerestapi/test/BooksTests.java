package com.fakerestapi.test;

import com.fakerestapi.test.clients.BookClient;
import com.fakerestapi.test.models.Book;
import com.fakerestapi.test.utils.BooksHelper;
import com.fakerestapi.test.validators.ResponseValidator;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

@Listeners({AllureTestNg.class})
public class BooksTests extends BaseTests {

    private BookClient bookClient;
    private BooksHelper booksFakerUtil;
    private Random random;

    @BeforeClass
    public void setUpBooksTests() {
        random = new Random();
        bookClient = new BookClient();
        booksFakerUtil = new BooksHelper();
    }

    @Test(description = "Get all books")
    @Step("")
    public void getAllBooks() {
        Response response = bookClient.getAllBooks();
        ResponseValidator.validateStatusCode(response, 200);
        ResponseValidator.validateListOfIdsNotEmpty(response);
    }

    @Test(description = "Get a book by a valid id")
    public void getBookByValidId() {
        Response allBooksResponse = bookClient.getAllBooks();
        ResponseValidator.validateStatusCode(allBooksResponse, 200);
        ResponseValidator.validateListOfIdsNotEmpty(allBooksResponse);

        int randomBookId = random.nextInt(bookClient.getAllBooks().jsonPath().getList("id", Integer.class).size());

        Response bookResponse = bookClient.getBookById(randomBookId);

        ResponseValidator.validateStatusCode(bookResponse, 200);

        Book retrievedBook = bookResponse.as(Book.class);

        Assert.assertEquals(retrievedBook.getId(), randomBookId, "The book id in the response doesn't match the expected id");
        Assert.assertNotNull(retrievedBook.getTitle(), "The book title should not be null.");
        Assert.assertNotNull(retrievedBook.getDescription(), "The book description should not be null.");
        Assert.assertNotNull(retrievedBook.getPageCount(), "The page count should not be empty");
        Assert.assertNotNull(retrievedBook.getExcerpt(), "The excerpt should not be empty");
        Assert.assertNotNull(retrievedBook.getPublishDate(), "The book publish date should not be null.");
    }

    @Test(description = "Get a book by invalid id")
    public void getBookByInvalidId() {
        int invalidBookId = bookClient.getAllBooks().jsonPath().getList("id", Integer.class).size() + 1;
        Response response = bookClient.getBookById(invalidBookId);
        ResponseValidator.validateStatusCode(response, 404);
        //add validation for error message text
    }

    @Test
    public void addNewBook() {;
        Book newBook = booksFakerUtil.createBookWithFakeData();

        Response postResponse = bookClient.addBook(newBook).then().extract().response();
        ResponseValidator.validateStatusCode(postResponse, 200);
        assertNotNull(postResponse.jsonPath().getString("id"), "The bookId should not be empty");

        Book createdBook = postResponse.as(Book.class);

        Assert.assertEquals(createdBook.getId(), newBook.getId());
        Assert.assertEquals(createdBook.getTitle(), newBook.getTitle());
        Assert.assertEquals(createdBook.getDescription(), newBook.getDescription());
        Assert.assertEquals(createdBook.getPageCount(), newBook.getPageCount());
        Assert.assertEquals(createdBook.getExcerpt(), newBook.getExcerpt());
        Assert.assertEquals(createdBook.getPublishDate(), newBook.getPublishDate());

    }

    @Test
    public void updateBookById() {
        Response getAllBooksResponse = bookClient.getAllBooks();
        ResponseValidator.validateStatusCode(getAllBooksResponse, 200);

        int id = random.nextInt(bookClient.getAllBooks().jsonPath().getList("id", Integer.class).size());

        Response bookForUpdate = bookClient.getBookById(id);
        Book beforeUpdateBook = bookForUpdate.as(Book.class);

        Book book = booksFakerUtil.createBookWithFakeData();

        Response updateBookResponse = bookClient.updateBook(id, book);

        Book updatedBook = updateBookResponse.as(Book.class);

        ResponseValidator.validateStatusCode(updateBookResponse, 200);
        assertNotEquals(updatedBook.getId(), beforeUpdateBook.getId());
        assertNotEquals(updatedBook.getTitle(), beforeUpdateBook.getTitle());
        assertNotEquals(updatedBook.getDescription(), beforeUpdateBook.getDescription());
        assertNotEquals(updatedBook.getPageCount(), beforeUpdateBook.getPageCount());
        assertNotEquals(updatedBook.getExcerpt(), beforeUpdateBook.getExcerpt());
        assertNotEquals(updatedBook.getPublishDate(), beforeUpdateBook.getPublishDate());
    }

    @Test
    public void deleteBookById() {
        int id = random.nextInt(bookClient.getAllBooks().jsonPath().getList("id", Integer.class).size());
        bookClient.deleteBook(id).then().statusCode(200);
        bookClient.getBookById(id).then().statusCode(404);

    }
}

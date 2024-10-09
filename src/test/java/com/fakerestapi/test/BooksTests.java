package com.fakerestapi.test;

import com.fakerestapi.test.clients.BookClient;
import com.fakerestapi.test.dataProviders.BooksDataProvider;
import com.fakerestapi.test.models.Book;
import com.fakerestapi.test.utils.BooksHelper;
import com.fakerestapi.test.utils.ErrorMessages;
import com.fakerestapi.test.validators.ResponseValidator;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

@Listeners({AllureTestNg.class})
public class BooksTests extends BaseTests {

    private BookClient bookClient;
    private BooksHelper booksHelper;
    private Gson gson;

    @BeforeClass
    public void setUpBooksTests() {
        bookClient = new BookClient();
        booksHelper = new BooksHelper();
        gson = new Gson();
    }

    @Test(description = "Verify that all books are successfully displayed")
    //@Step("")
    public void getAllBooksTest() {
        Response response = bookClient.getAllBooks();
        ResponseValidator.validateStatusCode(response, HttpStatus.SC_OK);
        ResponseValidator.validateListOfIdsNotEmpty(response);
    }

    @Test(description = "Verify that a book with valid id is displayed")
    public void getBookByValidIdTest() {
        Response allBooksResponse = bookClient.getAllBooks();
        ResponseValidator.validateStatusCode(allBooksResponse, HttpStatus.SC_OK);
        ResponseValidator.validateListOfIdsNotEmpty(allBooksResponse);

        int randomBookId = booksHelper.getRandomIdFromAllBooks();

        Response bookResponse = bookClient.getBookById(randomBookId);

        ResponseValidator.validateStatusCode(bookResponse, HttpStatus.SC_OK);

        Book retrievedBook = bookResponse.as(Book.class);

        Assert.assertEquals(retrievedBook.getId(), randomBookId, "The book id in the response doesn't match the expected id");
        Assert.assertNotNull(retrievedBook.getTitle(), "The book title should not be null.");
        Assert.assertNotNull(retrievedBook.getDescription(), "The book description should not be null.");
        Assert.assertNotNull(retrievedBook.getExcerpt(), "The excerpt should not be empty");
        Assert.assertNotNull(retrievedBook.getPublishDate(), "The book publish date should not be null.");
    }

    @Test(description = "Verify that a book with id bigger than the total count of the ids is not displayed")
    public void getBookByOutOfBoundsIdTest() {
        int invalidBookId = bookClient.getAllBooks().jsonPath().getList("id", Integer.class).size() + 1;
        Response response = bookClient.getBookById(invalidBookId);
        ResponseValidator.validateStatusCode(response, HttpStatus.SC_NOT_FOUND);
        ResponseValidator.validateErrorMessageTitle(response, ErrorMessages.ERROR_MESSAGE_NOT_FOUND);
    }

    @Test(description = "Verify that a new book with valid data is successfully added")
    public void addNewBookWithValidDataTest() {
        Book newBook = booksHelper.createBookWithFakeData();
        String newBookJson = gson.toJson(newBook);

        Response postResponse = bookClient.addBook(newBookJson).then().extract().response();
        ResponseValidator.validateStatusCode(postResponse, HttpStatus.SC_OK);
        assertNotNull(postResponse.jsonPath().getString("id"), "The bookId should not be empty");

        Book createdBook = postResponse.as(Book.class);

        Assert.assertEquals(createdBook.getId(), newBook.getId());
        Assert.assertEquals(createdBook.getTitle(), newBook.getTitle());
        Assert.assertEquals(createdBook.getDescription(), newBook.getDescription());
        Assert.assertEquals(createdBook.getPageCount(), newBook.getPageCount());
        Assert.assertEquals(createdBook.getExcerpt(), newBook.getExcerpt());
        Assert.assertEquals(createdBook.getPublishDate(), newBook.getPublishDate());
    }

    @Test(description = "Verify that a new book with empty values cannot be created")
    public void addNewBookWithEmptyDataTest() {
        Book newBook = booksHelper.createBookWithEmptyData();
        String newBookJson = gson.toJson(newBook);

        Response response = bookClient.addBook(newBookJson).then().extract().response();
        ResponseValidator.validateStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        ResponseValidator.validateErrorMessageTitle(response, ErrorMessages.ERROR_MESSAGE_VALIDATION_ERRORS);
    }

    @Test(description = "Verify that a new book with different invalid values for id cannot be added ",
            dataProvider = "createBookWithInvalidIds",
            dataProviderClass = BooksDataProvider.class)
    public void addNewBookWithInvalidIdsTest(Object id, int expectedStatusCode, String errorMessage) {
        Book book = booksHelper.createBookWithFakeData();
        book.setId(id);

        String bookJson = gson.toJson(book);
        Response response = bookClient.addBook(bookJson).then().extract().response();

        ResponseValidator.validateStatusCode(response, expectedStatusCode);
    }

    @Test(description = "Verify that a new book with different invalid values for page count cannot be added ",
            dataProvider = "createBookWithInvalidPageCountValue",
            dataProviderClass = BooksDataProvider.class)
    public void addNewBookWithInvalidPageCountTest(Object pageCount, int expectedStatusCode, String errorMessage) {
        Book book = booksHelper.createBookWithFakeData();
        book.setPageCount(pageCount);

        String bookJson = gson.toJson(book);
        Response response = bookClient.addBook(bookJson).then().extract().response();

        ResponseValidator.validateStatusCode(response, expectedStatusCode);
    }

    @Test(description = "Verify that a new book with different invalid values for publish date cannot be added",
            dataProvider = "createBookWithInvalidPublishDate",
            dataProviderClass = BooksDataProvider.class)
    public void addNewBookWithInvalidPublishDatesTest(String publishDate, int expectedStatusCode, String errorMessage) {
        Book book = booksHelper.createBookWithFakeData();
        book.setPublishDate(publishDate);

        String bookJson = gson.toJson(book);
        Response response = bookClient.addBook(bookJson).then().extract().response();

        ResponseValidator.validateStatusCode(response, expectedStatusCode);
    }

    @Test(description = "Update an existing book with valid id")
    public void updateBookByValidIdTest() {
        Response getAllBooksResponse = bookClient.getAllBooks();
        ResponseValidator.validateStatusCode(getAllBooksResponse, 200);

        int id = booksHelper.getRandomIdFromAllBooks();

        Response bookForUpdate = bookClient.getBookById(id);
        Book beforeUpdateBook = bookForUpdate.as(Book.class);

        Book book = booksHelper.createBookWithFakeData();
        String bookJson = gson.toJson(book);

        Response updateBookResponse = bookClient.updateBook(id, bookJson);

        Book updatedBook = updateBookResponse.as(Book.class);

        ResponseValidator.validateStatusCode(updateBookResponse, 200);
        assertNotEquals(updatedBook.getId(), beforeUpdateBook.getId());
        assertNotEquals(updatedBook.getTitle(), beforeUpdateBook.getTitle());
        assertNotEquals(updatedBook.getDescription(), beforeUpdateBook.getDescription());
        assertNotEquals(updatedBook.getPageCount(), beforeUpdateBook.getPageCount());
        assertNotEquals(updatedBook.getExcerpt(), beforeUpdateBook.getExcerpt());
        assertNotEquals(updatedBook.getPublishDate(), beforeUpdateBook.getPublishDate());
    }

    @Test(description = "Verify that a book with invalid values for id cannot be updated",
            dataProvider = "createBookWithInvalidIds",
            dataProviderClass = BooksDataProvider.class)
    public void updateBookWithInvalidIdsTests(Object id, int expectedStatusCode, String errorMessage) {
        Book book = booksHelper.createBookWithFakeData();
        book.setId(id);

        String bookJson = gson.toJson(book);
        Response response = bookClient.updateBook(id, bookJson).then().extract().response();

        ResponseValidator.validateStatusCode(response, expectedStatusCode);
    }

    @Test(description = "Verify that a book with invalid values for page count cannot be updated",
            dataProvider = "createBookWithInvalidPageCountValue",
            dataProviderClass = BooksDataProvider.class)
    public void updateBookWithInvalidPageCountTests(Object pageCount, int expectedStatusCode, String errorMessage) {
        Response getAllBooksResponse = bookClient.getAllBooks();
        ResponseValidator.validateStatusCode(getAllBooksResponse, HttpStatus.SC_OK);

        int id = booksHelper.getRandomIdFromAllBooks();

        Book book = booksHelper.createBookWithFakeData();
        book.setPageCount(pageCount);

        String bookJson = gson.toJson(book);
        Response response = bookClient.updateBook(id, bookJson).then().extract().response();

        ResponseValidator.validateStatusCode(response, expectedStatusCode);
    }

    @Test(description = "Verify that a book with different invalid values for page count cannot be updated",
            dataProvider = "createBookWithInvalidPublishDate",
            dataProviderClass = BooksDataProvider.class)
    public void updateBookWithInvalidPublishDatesTests(String publishDate, int expectedStatusCode, String errorMessage) {
        Response getAllBooksResponse = bookClient.getAllBooks();
        ResponseValidator.validateStatusCode(getAllBooksResponse, HttpStatus.SC_OK);

        int id = booksHelper.getRandomIdFromAllBooks();

        Book book = booksHelper.createBookWithFakeData();
        book.setPublishDate(publishDate);

        String bookJson = gson.toJson(book);
        Response response = bookClient.updateBook(id, bookJson).then().extract().response();

        ResponseValidator.validateStatusCode(response, expectedStatusCode);
    }

    @Test(description = "Verify that a book is deleted after using an existing id")
    public void deleteBookByIdTest() {
        int id = booksHelper.getRandomIdFromAllBooks();
        bookClient.deleteBook(id).then().statusCode(HttpStatus.SC_OK);
        bookClient.getBookById(id).then().statusCode(HttpStatus.SC_BAD_REQUEST);

    }

    //delete book with invalid id - number out of range

    //delete book with invalid id - invalid id format - ex 1a
}

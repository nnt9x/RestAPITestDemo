import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.internal.path.json.JSONAssertion;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import ro.skyah.comparator.JSONCompare;

public class TestBookStoreAPI {

    private static final String BASE_URL = "https://demoqa.com/";

    @BeforeAll
    public static void initEnv() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("status code nen bang 200")
    public void getBookListStatusCode200() {
        // Tạo request lên server -> lấy response
        // Validate các trường thông tin trong response
        Response response = RestAssured.given()
                .when().get("/BookStore/v1/Books");

//        Assertions.assertEquals(201, response.getStatusCode());
        response.then().statusCode(200)
                .and().contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("Book with ISBN 1233")
    public void getBookWithISBN1233(){
        Response response = RestAssured.given()
                .when().param("ISBN","1233").get("/BookStore/v1/Book");

        // Khoong tim thay status code 400
        response.then().statusCode(400).contentType(ContentType.JSON);
        response.getBody().prettyPrint();
    }

    @Test
    @DisplayName("Book with ISBN 9781449325862")
    public void getBookWithISBN9781449325862(){
        Response response = RestAssured.given()
                .when().param("ISBN","9781449325862")
                .get("/BookStore/v1/Book");

        // Validate Status code + content type
        response.then().statusCode(200)
                .contentType(ContentType.JSON);

        // Validate body (payload)
        String body = response.getBody().asString();

        // Print console
        response.body().prettyPrint();

        String bodyExpected = "{\n" +
                "      \"isbn\": \"9781449325862\",\n" +
                "      \"title\": \"Git Pocket Guide\",\n" +
                "      \"subTitle\": \"A Working Introduction\",\n" +
                "      \"author\": \"Richard E. Silverman\",\n" +
                "      \"publish_date\": \"2020-06-04T08:48:39.000Z\",\n" +
                "      \"publisher\": \"O'Reilly Media\",\n" +
                "      \"pages\": 234,\n" +
                "      \"description\": \"This pocket guide is the perfect on-the-job companion to Git, the distributed version control system. It provides a compact, readable introduction to Git for new users, as well as a reference to common commands and procedures for those of you with Git exp\",\n" +
                "      \"website\": \"http://chimera.labs.oreilly.com/books/1230000000561/index.html\"\n" +
                "    }";

        JSONCompare.assertEquals(bodyExpected, body);
    }

}

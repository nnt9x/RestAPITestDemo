import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class BookStoreSteps {

    // Cucumber Expression hoặc Regular Expression
    // Chỉ chọn 1 trong 2 cách viết, ko mix cả hai
    // Tạo đối tượng JSON để lưu trữ thông tin

    private JSONObject userParams = new JSONObject();
    private Response responseActual;

    private String token;

    private static final String MY_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6ImRlbW9Vc2VyMzQyIiwicGFzc3dvcmQiOiJCa2FjYWRAMTIzIiwiaWF0IjoxNjUzOTEwNDE0fQ.696wanNvvM3xTISYZES26HxIKKt2KpgcuFC2YqIsuts";
    private static final String USER_ID = "480fb775-4fe1-4be3-aa3d-66109ca814a8";

    @Given("cho userName hop le {string}")
    public void taoTaiKhoanMoi(String userName) {
        userParams.put("userName", userName);
    }

    @And("cho password hop le {string}")
    public void choPasswordHopLe(String password) {
        userParams.put("password", password);
    }


    @When("tao tai khoan nguoi dung")
    public void taoTaiKhoanNguoiDung() {
        // Tạo request lấy response -> validate

        // Config Request
        RestAssured.baseURI = "https://demoqa.com";
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.body(userParams.toString());

        // Gửi lên server
        responseActual = request.post("/Account/v1/User");
        // Validate: statusCode, header....

        /* JUNIT 5
        int statusCode = responseActual.getStatusCode();
        Assertions.assertEquals(201, statusCode);
         */
        // Hoặc
        responseActual.then()
                .statusCode(201)
                .contentType(ContentType.JSON);

        // In response
        responseActual.prettyPrint();
    }

    @Then("lay ra userID")
    public void layRaUserID() {
        String userID = responseActual.getBody().jsonPath().get("userID");
        System.out.println(userID);
    }

    @When("thuc hien lay token")
    public void thucHienLayToken() {
        // Config
        RestAssured.baseURI = "https://demoqa.com";
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.body(userParams.toString());

        // Gửi server -> lấy response -> validate.
        responseActual = request.post("/Account/v1/GenerateToken");
        responseActual.then().statusCode(200)
                .contentType(ContentType.JSON);
        responseActual.prettyPrint();
    }


    @Then("in ra token")
    public void inRaToken() {
        token = responseActual.getBody().jsonPath().get("token");
        System.out.println(token);
    }


    @Given("tai khoan da xac thuc")
    public void taiKhoanDaXacThuc() {

    }


    @When("them mot cuon sach ma ISBN {string}")
    public void themMotCuonSachMaISBN(String isbn) {

        String body = String.format(
                "{\n" +
                        "    \"userId\": \"%s\",\n" +
                        "    \"collectionOfIsbns\": [\n" +
                        "        {\n" +
                        "            \"isbn\": \"%s\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}"
                , USER_ID, isbn);


        RestAssured.baseURI = "https://demoqa.com";
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON)
                .auth().oauth2(MY_TOKEN)
                .body(body);

        responseActual = request.post("/BookStore/v1/Books");
        responseActual.prettyPrint();
        responseActual.then()
                .statusCode(201)
                .contentType(ContentType.JSON);

    }

    @Then("cuon sach duoc them vao tai khoan")
    public void cuonSachDuocThemVaoTaiKhoan() {
        List<String> isbns = responseActual.body().jsonPath()
                        .getList("books.isbn");

        System.out.println(isbns);
    }
}

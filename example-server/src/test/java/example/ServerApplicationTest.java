package example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class ServerApplicationTest {

    @Test
    void mainApp() {
        RestAssured.port = 8098;
        ServerApplication.main(new String[]{String.format("--server.port=%s", RestAssured.port)});

        given()
                .when()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get("/internal/health")
                .then()
                .body("status", equalTo("UP"));
    }
}
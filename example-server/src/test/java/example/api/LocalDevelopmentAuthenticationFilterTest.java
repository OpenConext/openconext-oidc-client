package example.api;

import io.restassured.http.ContentType;
import example.AbstractTest;
import example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("local")
class LocalDevelopmentAuthenticationFilterTest extends AbstractTest {

    @Test
    void meMockUser() {
        User user = given()
                .when()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get("/api/v1/users/me")
                .as(User.class);
        assertEquals("urn:collab:person:example.com:super", user.getSub());
    }

}
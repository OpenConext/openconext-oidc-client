package example.api;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import example.AbstractTest;
import example.AccessCookieFilter;
import example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest extends AbstractTest {

    @SneakyThrows
    @Test
    void me() {
        AccessCookieFilter accessCookieFilter = openIDConnectFlow("/api/v1/users/me", "urn:collab:person:example.com:admin");

        User user = given()
                .when()
                .filter(accessCookieFilter.cookieFilter())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get(accessCookieFilter.apiURL())
                .as(User.class);
        assertEquals("urn:collab:person:example.com:admin", user.getEmail());
    }

    @SneakyThrows
    @Test
    void meForceLogin() {
        AccessCookieFilter accessCookieFilter = openIDConnectFlow("/api/v1/users/attributes?force=true",
                "urn:collab:person:example.com:admin",
                authorizationUrl ->
                        assertEquals("login", UriComponentsBuilder.fromUriString(authorizationUrl).build().getQueryParams().getFirst("prompt"))
                , m -> {
                    m.put("custom", "val");
                    return m;
                });

        Map<String, Object> user = given()
                .when()
                .filter(accessCookieFilter.cookieFilter())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get(accessCookieFilter.apiURL())
                .as(new TypeRef<>() {
                });
        assertEquals("val", user.get("custom"));
    }

    @SneakyThrows
    @Test
    void logout() {
        AccessCookieFilter accessCookieFilter = openIDConnectFlow("/api/v1/users/me", "urn:collab:person:example.com:admin");

        User user = given()
                .when()
                .filter(accessCookieFilter.cookieFilter())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get(accessCookieFilter.apiURL())
                .as(User.class);
        assertEquals("urn:collab:person:example.com:admin", user.getEmail());

        String location = given()
                .redirects()
                .follow(false)
                .when()
                .filter(accessCookieFilter.cookieFilter())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get("/api/v1/users/logout")
                .header("Location");
        assertEquals("http://localhost:" + super.port + "/api/v1/users/me", location);

        location = given()
                .when()
                .redirects()
                .follow(false)
                .filter(accessCookieFilter.cookieFilter())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get(UriComponentsBuilder.fromUriString(location).build().getPath())
                .header("Location");
        assertEquals("http://localhost:" + super.port + "/oauth2/authorization/oidcng", location);

    }

}
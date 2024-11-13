package oidc.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomOidcUserServiceTest {

    @RegisterExtension
    WireMockExtension mockServer = new WireMockExtension(8081);

    @Test
    void loadUser() throws JsonProcessingException {
        CustomOidcUserService oidcUserService = new CustomOidcUserService(oidcUser -> {
        });
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId("client_id")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .tokenUri("http://localhost:8081/token")
                .authorizationUri("http://localhost:8081/authorize")
                .redirectUri("http://localhost:8081/redirect")
                .userInfoUri("http://localhost:8081/user-info")
                .clientId("client_id")
                .userNameAttributeName("sub")
                .build();
        Instant now = Instant.now();
        Instant expiresAt = now.plus(1, ChronoUnit.HOURS);
        Map<String, Object> claims = Map.of(
                "eduperson_principal_name", "jot@example.com",
                "email", "jot@example.com",
                "family_name", "Doe",
                "given_name", "Jot",
                "schac_home_organization", "example.com",
                "scope", "openid",
                "sub", "sub"
        );

        OidcIdToken idToken = new OidcIdToken("value", now, expiresAt, claims);
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "token", now, expiresAt);
        OidcUserRequest request = new OidcUserRequest(clientRegistration, accessToken, idToken);

        String userInfoResult = new ObjectMapper().writeValueAsString(claims);
        stubFor(get(urlPathMatching("/user-info")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(userInfoResult)));

        OidcUser oidcUser = oidcUserService.loadUser(request);
        Object edupersonPrincipalName = oidcUser.getClaims().get("eduperson_principal_name");

        assertEquals(edupersonPrincipalName, "jot@example.com");
    }
}
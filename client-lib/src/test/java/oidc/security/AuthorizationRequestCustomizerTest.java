package oidc.security;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest.Builder;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AuthorizationRequestCustomizerTest {

    @Test
    void accept() {
        AuthorizationRequestCustomizer authorizationRequestCustomizer =
                new AuthorizationRequestCustomizer();
        //Set up the request mocking
        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "http://localhost/login");
        request.addParameter("force", "true");
        request.getSession(true).setAttribute("SPRING_SECURITY_SAVED_REQUEST",
                new DefaultSavedRequest(request, new PortResolverImpl()));
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);
        Builder builder = OAuth2AuthorizationRequest
                .authorizationCode()
                .clientId("client_id")
                .authorizationUri("http://localhost/authorize");

        authorizationRequestCustomizer.accept(builder);

        String prompt = UriComponentsBuilder.fromUriString(builder.build().getAuthorizationRequestUri()).build().getQueryParams().getFirst("prompt");
        assertEquals("login", prompt);
    }

    @Test
    void acceptNoSession() {
        AuthorizationRequestCustomizer authorizationRequestCustomizer =
                new AuthorizationRequestCustomizer();
        //Set up the request mocking
        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "http://localhost/login");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Builder builder = OAuth2AuthorizationRequest
                .authorizationCode()
                .clientId("client_id")
                .authorizationUri("http://localhost/authorize");

        authorizationRequestCustomizer.accept(builder);

        boolean hasPrompt = UriComponentsBuilder.fromUriString(builder.build().getAuthorizationRequestUri()).build().getQueryParams().containsKey("prompt");
        assertFalse(hasPrompt);
    }
}
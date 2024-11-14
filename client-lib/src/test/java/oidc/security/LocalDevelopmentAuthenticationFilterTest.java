package oidc.security;

import jakarta.servlet.ServletException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LocalDevelopmentAuthenticationFilterTest {

    @Test
    void doFilter() throws ServletException, IOException {
        new LocalDevelopmentAuthenticationFilter()
                .doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), new MockFilterChain());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object sub = ((DefaultOidcUser) authentication.getPrincipal()).getClaims().get("sub");
        assertEquals(sub,"urn:collab:person:example.com:super");
    }
}
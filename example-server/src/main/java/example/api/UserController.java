package example.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import example.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
@RequestMapping(value = {"/api/v1/users"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private static final Log LOG = LogFactory.getLog(UserController.class);

    @GetMapping("/attributes")
    public ResponseEntity<Map<String, Object>> attributes(@AuthenticationPrincipal OidcUser oidcUser) {
        LOG.debug(String.format("/attributes for user %s", oidcUser.getClaims().get("sub")));

        return ResponseEntity.ok(oidcUser.getAttributes());
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(@AuthenticationPrincipal OidcUser oidcUser) {
        LOG.debug(String.format("/me for user %s", oidcUser.getClaims().get("sub")));

        return ResponseEntity.ok(new User(oidcUser.getAttributes()));
    }

    @GetMapping("/logout")
    public View logout(@AuthenticationPrincipal OidcUser oidcUser, HttpServletRequest request) throws ServletException {
        LOG.debug(String.format("/logout for user %s", oidcUser.getClaims().get("sub")));

        request.getSession().invalidate();
        request.logout();
        SecurityContextHolder.clearContext();

        return new RedirectView("/api/v1/users/me?force=true", true);
    }


}

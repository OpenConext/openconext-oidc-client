package oidc.security;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface UserProvisioning {

    void provision(OidcUser oidcUser);

}

package oidc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@SuppressWarnings("unchecked")
public class User implements Serializable {

    private String sub;
    private String eduPersonPrincipalName;
    private String givenName;
    private String familyName;
    private String subjectId;
    private String eduId;
    private String uid;
    private String schacHomeOrganization;
    private String email;
    private Instant createdAt;
    private Instant lastActivity;

    /**
     * Construct a user. If you miss attributes, then extend this class
     *
     * @param attributes the claims of the user-info endpoint
     */
    public User(Map<String, Object> attributes) {
        this.sub = (String) attributes.get("sub");
        this.eduPersonPrincipalName = (String) attributes.get("eduperson_principal_name");
        this.schacHomeOrganization = (String) attributes.get("schac_home_organization");
        this.email = (String) attributes.get("email");
        this.givenName = (String) attributes.get("given_name");
        this.familyName = (String) attributes.get("family_name");
        this.subjectId = (String) attributes.get("subject_id");
        this.eduId = (String) attributes.get("eduid");
        this.uid = ((List<String>) attributes.getOrDefault("uids", List.of())).stream().findAny().orElse(null);
        this.createdAt = Instant.now();
        this.lastActivity = this.createdAt;
    }

}

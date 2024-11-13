package example.model;

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
public class User extends oidc.model.User {

    private List<String> memberships;

    public User(Map<String, Object> attributes) {
        super(attributes);
        this.memberships = (List<String>) attributes.getOrDefault("edumember_is_member_of", List.of());
    }

}

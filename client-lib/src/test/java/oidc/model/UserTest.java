package oidc.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructor() {
        //Just assert not exceptions are thrown. That is for subclasses
        User user = new User(Map.of());
        assertNotNull(user.getCreatedAt());
    }
}
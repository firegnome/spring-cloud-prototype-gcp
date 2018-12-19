package ch.hsr.sai14sa12.commentmicroservice.security;

import java.util.UUID;

public class UserDetails {
    private UUID userId;
    private String username;

    public UserDetails(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}

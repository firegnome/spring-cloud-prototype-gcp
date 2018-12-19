package ch.hsr.sai14sa12.authmicroservice.user.dto;

import java.util.UUID;

public class UserInfoDto {
    private UUID id;
    private String username;

    public UserInfoDto(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}

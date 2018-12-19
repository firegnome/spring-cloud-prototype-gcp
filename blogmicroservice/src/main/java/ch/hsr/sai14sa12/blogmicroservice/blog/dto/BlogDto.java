package ch.hsr.sai14sa12.blogmicroservice.blog.dto;

import java.time.Instant;
import java.util.UUID;

public class BlogDto {
    private long id;
    private UUID userId;
    private String name;
    private String description;
    private Instant created;

    public BlogDto(long id, UUID userId, String name, String description, Instant created) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.created = created;
    }

    public long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreated() {
        return created;
    }
}

package ch.hsr.sai14sa12.blogmicroservice.blogpost.dto;

import java.time.Instant;
import java.util.UUID;

public class BlogPostDto {
    private long id;
    private UUID userId;
    private String title;
    private String content;
    private Instant created;

    public BlogPostDto(long id, UUID userId, String title, String content, Instant created) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.created = created;
    }

    public long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreated() {
        return created;
    }
}

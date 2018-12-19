package ch.hsr.sai14sa12.commentmicroservice.blogpostcomment.dto;

import java.time.Instant;
import java.util.UUID;

public class BlogPostCommentDto {
    private long id;
    private UUID userId;
    private String comment;
    private Instant created;

    public BlogPostCommentDto(long id, UUID userId, String comment, Instant created) {
        this.id = id;
        this.userId = userId;
        this.comment = comment;
        this.created = created;
    }

    public long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }

    public Instant getCreated() {
        return created;
    }
}

package ch.hsr.sai14sa12.blogmicroservice.blogpost.dto;

import java.time.Instant;
import java.util.UUID;

public class BlogPostReadEventDto {
    private long blogId;
    private long postId;
    private UUID userId;
    private Instant readTime;

    public BlogPostReadEventDto(long blogId, long postId, UUID userId, Instant readTime) {
        this.blogId = blogId;
        this.postId = postId;
        this.userId = userId;
        this.readTime = readTime;
    }

    public long getBlogId() {
        return blogId;
    }

    public long getPostId() {
        return postId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getReadTime() {
        return readTime;
    }
}

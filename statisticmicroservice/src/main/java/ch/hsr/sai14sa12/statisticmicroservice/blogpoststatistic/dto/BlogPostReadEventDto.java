package ch.hsr.sai14sa12.statisticmicroservice.blogpoststatistic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BlogPostReadEventDto {
    public static final String BLOG_ID = "blogId";
    public static final String POST_ID = "postId";
    public static final String USER_ID = "userId";
    public static final String READ_TIME = "readTime";

    private long blogId;
    private long postId;
    private UUID userId;
    private Instant readTime;
}

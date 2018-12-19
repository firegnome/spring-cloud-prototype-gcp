package ch.hsr.sai14sa12.commentmicroservice.blogpostcomment;

import ch.hsr.sai14sa12.commentmicroservice.blogpostcomment.dto.BlogPostCommentDto;
import com.google.cloud.datastore.Entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class BlogPostCommentTransformer {
    private BlogPostCommentTransformer() {
    }

    static BlogPostCommentDto entityToBlogPostCommentDto(Entity entity) {
        return new BlogPostCommentDto(
                entity.getKey().getId(),
                UUID.fromString(entity.getString("userId")),
                entity.getString("comment"),
                Instant.ofEpochMilli(entity.getLong("created"))
        );
    }

    static List<BlogPostCommentDto> entityIteratorToBlogPostCommentDtos(Iterator<Entity> blogPostCommentEntities) {
        List<BlogPostCommentDto> blogPostCommentDtos = new ArrayList<>();
        while (blogPostCommentEntities.hasNext()) {
            Entity commentEntity = blogPostCommentEntities.next();
            blogPostCommentDtos.add(entityToBlogPostCommentDto(commentEntity));
        }
        return blogPostCommentDtos;
    }
}

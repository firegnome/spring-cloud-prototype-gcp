package ch.hsr.sai14sa12.blogmicroservice.blogpost;

import ch.hsr.sai14sa12.blogmicroservice.blogpost.dto.BlogPostDto;
import com.google.cloud.datastore.Entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

class BlogPostTransformer {
    private BlogPostTransformer() {
    }

    static BlogPostDto entityToBlogPostDto(Entity entity) {
        return new BlogPostDto(
                entity.getKey().getId(),
                UUID.fromString(entity.getString("userId")),
                entity.getString("title"),
                entity.getString("content"),
                Instant.ofEpochMilli(entity.getLong("created"))
        );
    }

    static List<BlogPostDto> entityIteratorToBlogPostDtos(Iterator<Entity> blogPostEntities) {
        List<BlogPostDto> blogPostDtos = new ArrayList<>();
        while (blogPostEntities.hasNext()) {
            Entity blogEntity = blogPostEntities.next();
            blogPostDtos.add(entityToBlogPostDto(blogEntity));
        }
        return blogPostDtos;
    }
}

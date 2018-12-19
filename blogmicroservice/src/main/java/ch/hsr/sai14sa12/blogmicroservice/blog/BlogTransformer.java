package ch.hsr.sai14sa12.blogmicroservice.blog;

import ch.hsr.sai14sa12.blogmicroservice.blog.dto.BlogDto;
import com.google.cloud.datastore.Entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class BlogTransformer {

    static BlogDto entityToBlogDto(Entity entity) {
        return new BlogDto(
                entity.getKey().getId(),
                UUID.fromString(entity.getString("userId")),
                entity.getString("name"),
                entity.getString("description"),
                Instant.ofEpochMilli(entity.getLong("created")));
    }

    static List<BlogDto> entityIteratorToBlogDtos(Iterator<Entity> blogEntities) {
        List<BlogDto> blogOverviewDtos = new ArrayList<>();
        while (blogEntities.hasNext()) {
            Entity blogEntity = blogEntities.next();
            blogOverviewDtos.add(entityToBlogDto(blogEntity));
        }
        return blogOverviewDtos;
    }
}

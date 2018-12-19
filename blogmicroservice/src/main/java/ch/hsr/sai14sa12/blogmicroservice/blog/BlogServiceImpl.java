package ch.hsr.sai14sa12.blogmicroservice.blog;


import ch.hsr.sai14sa12.blogmicroservice.blog.dto.BlogDto;
import ch.hsr.sai14sa12.blogmicroservice.blog.exception.BlogNotFoundException;
import com.google.cloud.datastore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class BlogServiceImpl implements BlogService {
    public static final String BLOG_KIND = "blog";
    private static final Logger log = LoggerFactory.getLogger(BlogServiceImpl.class);
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private final KeyFactory blogKeyFactory = datastore.newKeyFactory().setKind(BLOG_KIND);

    @Override
    @CacheEvict(value = "cached-blogs", allEntries = true)
    public void createBlog(String name, String description, UUID userId) {
        Key blogKey = datastore.allocateId(blogKeyFactory.newKey());
        Instant now = Instant.now();

        Entity blogEntity = Entity.newBuilder(blogKey)
                .set("name", name)
                .set("description", description)
                .set("created", now.toEpochMilli())
                .set("userId", userId.toString())
                .build();
        datastore.put(blogEntity);

        log.info("user with id " + userId + " created new blog with name: " + name);
    }

    @Override
    @Cacheable(value = "cached-blogs")
    public List<BlogDto> getAllBlogs() {
        Query<Entity> query = Query.newEntityQueryBuilder().setKind(BLOG_KIND).build();
        return BlogTransformer.entityIteratorToBlogDtos(datastore.run(query));
    }

    @Override
    public BlogDto getBlogById(long id) {
        Entity blogEntity = datastore.get(blogKeyFactory.newKey(id));
        if (blogEntity == null) {
            log.warn("blog with id " + id + " not found!");
            throw new BlogNotFoundException();
        }
        return BlogTransformer.entityToBlogDto(blogEntity);
    }
}

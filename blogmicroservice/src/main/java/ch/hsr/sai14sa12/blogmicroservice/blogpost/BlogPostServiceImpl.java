package ch.hsr.sai14sa12.blogmicroservice.blogpost;

import ch.hsr.sai14sa12.blogmicroservice.blog.BlogProcessor;
import ch.hsr.sai14sa12.blogmicroservice.blog.BlogService;
import ch.hsr.sai14sa12.blogmicroservice.blog.BlogServiceImpl;
import ch.hsr.sai14sa12.blogmicroservice.blog.dto.BlogDto;
import ch.hsr.sai14sa12.blogmicroservice.blogpost.dto.BlogPostDto;
import ch.hsr.sai14sa12.blogmicroservice.blogpost.dto.BlogPostReadEventDto;
import ch.hsr.sai14sa12.blogmicroservice.blogpost.dto.CreateBlogPostDto;
import ch.hsr.sai14sa12.blogmicroservice.security.UserDetails;
import ch.hsr.sai14sa12.blogmicroservice.security.exception.UserNotAuthorizedException;
import com.google.cloud.datastore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static ch.hsr.sai14sa12.blogmicroservice.blogpost.BlogPostTransformer.entityIteratorToBlogPostDtos;
import static ch.hsr.sai14sa12.blogmicroservice.blogpost.BlogPostTransformer.entityToBlogPostDto;

@Service
@EnableBinding(BlogProcessor.class)
public class BlogPostServiceImpl implements BlogPostService {

    private static final String BLOG_POST_KIND = "blog-post";
    private static final Logger log = LoggerFactory.getLogger(BlogServiceImpl.class);
    private final BlogProcessor blogProcessor;
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private BlogService blogService;

    public BlogPostServiceImpl(BlogProcessor blogProcessor, BlogService blogService) {
        this.blogProcessor = blogProcessor;
        this.blogService = blogService;
    }

    @Override
    public void createBlogPost(CreateBlogPostDto createBlogPostDto, long blogId, UUID userId) {
        BlogDto blogDto = this.blogService.getBlogById(blogId);
        if (blogDto == null) {
            return;
        }

        if (!blogDto.getUserId().equals(userId)) {
            log.warn("user " + userId + " is not authorized to add a blog post");
            throw new UserNotAuthorizedException();
        }

        Key blogPostKey = datastore.allocateId(
                datastore.newKeyFactory().setKind(BLOG_POST_KIND)
                        .addAncestor(PathElement.of(BlogServiceImpl.BLOG_KIND, blogId))
                        .newKey());

        Instant now = Instant.now();

        Entity blogPostEntity = Entity.newBuilder(blogPostKey)
                .set("title", createBlogPostDto.getTitle())
                .set("content", createBlogPostDto.getContent())
                .set("created", now.toEpochMilli())
                .set("userId", userId.toString())
                .build();
        datastore.put(blogPostEntity);

        log.info("user with id " + userId + " created new blog post with title: " + createBlogPostDto.getTitle());
    }

    @Override
    public List<BlogPostDto> getBlogPostsByBlogId(long blogId) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(BLOG_POST_KIND)
                .setFilter(StructuredQuery.PropertyFilter.hasAncestor(
                        datastore.newKeyFactory().setKind(BlogServiceImpl.BLOG_KIND).newKey(blogId)))
                .build();
        // Run query with eventual consistency, this is not default in ancestor relations: https://cloud.google.com/datastore/docs/concepts/structuring_for_strong_consistency#datastore_read_policy
        return entityIteratorToBlogPostDtos(datastore.run(query, ReadOption.eventualConsistency()));
    }

    @Override
    public BlogPostDto getBlogPostById(long blogId, long blogPostId, UserDetails userDetails) {
        BlogPostDto blogPostDto = entityToBlogPostDto(datastore.get(
                datastore.newKeyFactory().setKind(BLOG_POST_KIND)
                        .addAncestor(PathElement.of(BlogServiceImpl.BLOG_KIND, blogId))
                        .newKey(blogPostId)));
        blogProcessor.blogEvents().send(new GenericMessage<>(
                new BlogPostReadEventDto(
                        blogId,
                        blogPostId,
                        userDetails != null ? userDetails.getUserId() : null,
                        Instant.now())));
        return blogPostDto;
    }
}

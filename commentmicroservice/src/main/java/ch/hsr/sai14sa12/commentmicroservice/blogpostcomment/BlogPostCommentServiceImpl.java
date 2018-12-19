package ch.hsr.sai14sa12.commentmicroservice.blogpostcomment;

import ch.hsr.sai14sa12.commentmicroservice.blogpostcomment.dto.BlogPostCommentDto;
import ch.hsr.sai14sa12.commentmicroservice.blogpostcomment.dto.CreateBlogPostCommentDto;
import com.google.cloud.datastore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static ch.hsr.sai14sa12.commentmicroservice.blogpostcomment.BlogPostCommentTransformer.entityIteratorToBlogPostCommentDtos;

@Service
public class BlogPostCommentServiceImpl implements BlogPostCommentService {

    private static final String BLOG_KIND = "blog";
    private static final String BLOG_POST_KIND = "blog-post";
    private static final String BLOG_POST_COMMENT_KIND = "blog-post-comment";
    private static final Logger log = LoggerFactory.getLogger(BlogPostCommentServiceImpl.class);
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    @Override
    public void createBlogPostComment(CreateBlogPostCommentDto blogPostCommentDto, long blogId, long postId, UUID userId) {
        Key blogPostCommentKey = datastore.allocateId(
                datastore.newKeyFactory().setKind(BLOG_POST_COMMENT_KIND)
                        .addAncestor(PathElement.of(BLOG_KIND, blogId))
                        .addAncestor(PathElement.of(BLOG_POST_KIND, postId))
                        .newKey());

        Instant now = Instant.now();

        Entity blogPostCommentEntity = Entity.newBuilder(blogPostCommentKey)
                .set("comment", blogPostCommentDto.getComment())
                .set("created", now.toEpochMilli())
                .set("userId", userId.toString())
                .build();
        datastore.put(blogPostCommentEntity);

        log.info("user with id " + userId + " created new blog post comment: " + blogPostCommentDto.getComment());
    }

    @Override
    public List<BlogPostCommentDto> getBlogPostCommentsById(long blogId, long postId) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(BLOG_POST_COMMENT_KIND)
                .setFilter(StructuredQuery.PropertyFilter.hasAncestor(
                        datastore.newKeyFactory().setKind(BLOG_POST_KIND)
                                .addAncestor(PathElement.of(BLOG_KIND, blogId))
                                .newKey(postId)))
                .build();
        return entityIteratorToBlogPostCommentDtos(datastore.run(query, ReadOption.eventualConsistency()));
    }
}

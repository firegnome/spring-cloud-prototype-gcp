package ch.hsr.sai14sa12.statisticmicroservice.blogpoststatistic;

import ch.hsr.sai14sa12.statisticmicroservice.blogpoststatistic.dto.BlogPostReadEventDto;
import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.common.collect.Iterators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@EnableBinding(BlogPostStatisticProcessor.class)
@Slf4j
public class BlogPostStatisticServiceImpl implements BlogPostStatisticService {

    private static final String STATISTIC_BLOG_POST_VISIT_KIND = "statistic-blog-post-visit";
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private final KeyFactory blogPostStatisticKeyFactory = datastore.newKeyFactory().setKind(STATISTIC_BLOG_POST_VISIT_KIND);

    @StreamListener(BlogPostStatisticProcessor.INPUT)
    public void handle(BlogPostReadEventDto blogPostReadEventDto) {
        log.info("received visit of blog " + blogPostReadEventDto.getBlogId() + " post " + blogPostReadEventDto.getPostId());
        this.createBlogPostVisitStatistic(blogPostReadEventDto.getBlogId(), blogPostReadEventDto.getPostId(), blogPostReadEventDto.getUserId(), blogPostReadEventDto.getReadTime());
    }

    private void createBlogPostVisitStatistic(long blogId, long postId, UUID userId, Instant readTime) {
        Key key = datastore.allocateId(blogPostStatisticKeyFactory.newKey());
        Entity blogPostReadStatisticEntity = Entity.newBuilder(key)
                .set(BlogPostReadEventDto.BLOG_ID, blogId)
                .set(BlogPostReadEventDto.POST_ID, postId)
                .set(BlogPostReadEventDto.USER_ID, userId != null ? userId.toString() : "")
                .set(BlogPostReadEventDto.READ_TIME, readTime.toEpochMilli())
                .build();
        datastore.put(blogPostReadStatisticEntity);
    }

    @Override
    public int getVisitCountById(long blogId, long postId) {
        // because datastore doesn't provide a simple count function, a key-only query is performed and counted
        Query query = Query.newKeyQueryBuilder()
                .setKind(STATISTIC_BLOG_POST_VISIT_KIND)
                .setFilter(
                        CompositeFilter.and(
                                PropertyFilter.eq(BlogPostReadEventDto.BLOG_ID, blogId),
                                PropertyFilter.eq(BlogPostReadEventDto.POST_ID, postId)))
                .build();
        return Iterators.size(datastore.run(query));
    }
}

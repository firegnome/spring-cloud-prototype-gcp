package ch.hsr.sai14sa12.commentmicroservice.blogpostcomment;

import ch.hsr.sai14sa12.commentmicroservice.blogpostcomment.dto.BlogPostCommentDto;
import ch.hsr.sai14sa12.commentmicroservice.blogpostcomment.dto.CreateBlogPostCommentDto;
import ch.hsr.sai14sa12.commentmicroservice.security.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment/blog/{blogid}/post/{postid}")
public class BlogPostCommentController {

    private static final Logger log = LoggerFactory.getLogger(BlogPostCommentController.class);

    private final BlogPostCommentService blogPostCommentService;

    public BlogPostCommentController(@Autowired BlogPostCommentService blogPostCommentService) {
        this.blogPostCommentService = blogPostCommentService;
    }

    @GetMapping
    public List<BlogPostCommentDto> getBlogPostsByBlogId(@PathVariable("blogid") long blogId,
                                                         @PathVariable("postid") long postId) {
        return blogPostCommentService.getBlogPostCommentsById(blogId, postId);
    }

    @PostMapping
    public void createBlogPostComment(@RequestBody CreateBlogPostCommentDto createBlogPostCommentDto,
                                      @PathVariable("blogid") long blogId,
                                      @PathVariable("postid") long postId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        log.info("create blog post comment");
        blogPostCommentService.createBlogPostComment(createBlogPostCommentDto, blogId, postId, userDetails.getUserId());
    }
}

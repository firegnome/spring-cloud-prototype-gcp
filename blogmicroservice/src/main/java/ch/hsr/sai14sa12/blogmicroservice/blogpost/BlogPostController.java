package ch.hsr.sai14sa12.blogmicroservice.blogpost;

import ch.hsr.sai14sa12.blogmicroservice.blog.BlogServiceImpl;
import ch.hsr.sai14sa12.blogmicroservice.blogpost.dto.BlogPostDto;
import ch.hsr.sai14sa12.blogmicroservice.blogpost.dto.CreateBlogPostDto;
import ch.hsr.sai14sa12.blogmicroservice.security.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog/{blogId}/post")
public class BlogPostController {

    private static final Logger log = LoggerFactory.getLogger(BlogServiceImpl.class);

    private final BlogPostService blogPostService;

    public BlogPostController(@Autowired BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping
    public List<BlogPostDto> getBlogPostsByBlogId(@PathVariable("blogId") long blogId) {
        return blogPostService.getBlogPostsByBlogId(blogId);
    }

    @PostMapping
    public void createBlogPost(@RequestBody CreateBlogPostDto createBlogPostDto, @PathVariable("blogId") long blogId, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("create blog post");
        blogPostService.createBlogPost(createBlogPostDto, blogId, userDetails.getUserId());
    }

    @GetMapping("{id}")
    public BlogPostDto getBlogPost(@PathVariable("blogId") long blogId, @PathVariable("id") long id, @AuthenticationPrincipal UserDetails userDetails) {
        return this.blogPostService.getBlogPostById(blogId, id, userDetails);
    }
}

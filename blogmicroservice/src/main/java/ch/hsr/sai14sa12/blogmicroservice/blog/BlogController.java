package ch.hsr.sai14sa12.blogmicroservice.blog;

import ch.hsr.sai14sa12.blogmicroservice.blog.dto.BlogDto;
import ch.hsr.sai14sa12.blogmicroservice.blog.dto.CreateBlogDto;
import ch.hsr.sai14sa12.blogmicroservice.security.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    private static final Logger log = LoggerFactory.getLogger(BlogServiceImpl.class);

    private final BlogService blogService;

    public BlogController(@Autowired BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public List<BlogDto> getBlogs() {
        return blogService.getAllBlogs();
    }

    @PostMapping
    public void createBlog(@RequestBody CreateBlogDto createBlogDto, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("create blog");
        blogService.createBlog(createBlogDto.getName(), createBlogDto.getDescription(), userDetails.getUserId());
    }

    @GetMapping("{id}")
    public BlogDto getBlog(@PathVariable("id") long id) {
        return this.blogService.getBlogById(id);
    }
}

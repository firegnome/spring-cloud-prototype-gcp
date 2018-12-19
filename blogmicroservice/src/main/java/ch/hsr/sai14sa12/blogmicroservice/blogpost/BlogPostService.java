package ch.hsr.sai14sa12.blogmicroservice.blogpost;

import ch.hsr.sai14sa12.blogmicroservice.blogpost.dto.BlogPostDto;
import ch.hsr.sai14sa12.blogmicroservice.blogpost.dto.CreateBlogPostDto;
import ch.hsr.sai14sa12.blogmicroservice.security.UserDetails;

import java.util.List;
import java.util.UUID;

public interface BlogPostService {
    void createBlogPost(CreateBlogPostDto blogPostDto, long blogId, UUID userId);

    List<BlogPostDto> getBlogPostsByBlogId(long blogId);

    BlogPostDto getBlogPostById(long blogId, long blogPostId, UserDetails userDetails);
}

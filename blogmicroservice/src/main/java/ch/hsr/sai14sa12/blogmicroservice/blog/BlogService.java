package ch.hsr.sai14sa12.blogmicroservice.blog;

import ch.hsr.sai14sa12.blogmicroservice.blog.dto.BlogDto;

import java.util.List;
import java.util.UUID;

public interface BlogService {

    void createBlog(String title, String description, UUID userId);

    List<BlogDto> getAllBlogs();

    BlogDto getBlogById(long id);
}

package ch.hsr.sai14sa12.commentmicroservice.blogpostcomment;

import ch.hsr.sai14sa12.commentmicroservice.blogpostcomment.dto.BlogPostCommentDto;
import ch.hsr.sai14sa12.commentmicroservice.blogpostcomment.dto.CreateBlogPostCommentDto;

import java.util.List;
import java.util.UUID;

public interface BlogPostCommentService {
    void createBlogPostComment(CreateBlogPostCommentDto blogPostCommentDto, long blogId, long postId, UUID userId);

    List<BlogPostCommentDto> getBlogPostCommentsById(long blogId, long postId);
}
package ch.hsr.sai14sa12.commentmicroservice.blogpostcomment.dto;

public class CreateBlogPostCommentDto {
    private String comment;

    public CreateBlogPostCommentDto(String comment) {
        this.comment = comment;
    }

    public CreateBlogPostCommentDto() {
    }

    public String getComment() {
        return comment;
    }
}

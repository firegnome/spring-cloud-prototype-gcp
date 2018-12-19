package ch.hsr.sai14sa12.blogmicroservice.blogpost.dto;

public class CreateBlogPostDto {
    private String title;
    private String content;

    public CreateBlogPostDto() {
    }

    public CreateBlogPostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

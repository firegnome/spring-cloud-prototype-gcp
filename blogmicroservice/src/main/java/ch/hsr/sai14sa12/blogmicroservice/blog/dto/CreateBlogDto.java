package ch.hsr.sai14sa12.blogmicroservice.blog.dto;

public class CreateBlogDto {
    private String name;
    private String description;

    public CreateBlogDto() {
    }

    public CreateBlogDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

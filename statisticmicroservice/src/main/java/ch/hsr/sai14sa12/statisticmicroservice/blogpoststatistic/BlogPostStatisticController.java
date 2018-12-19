package ch.hsr.sai14sa12.statisticmicroservice.blogpoststatistic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistic/blog/{blogid}/post/{postid}")
@Slf4j
public class BlogPostStatisticController {

    private final BlogPostStatisticService blogPostStatisticService;

    public BlogPostStatisticController(@Autowired BlogPostStatisticService blogPostStatisticService) {
        this.blogPostStatisticService = blogPostStatisticService;
    }

    @GetMapping(path = "/visitcount")
    public int getVisitCountById(@PathVariable("blogid") long blogId,
                                 @PathVariable("postid") long postId) {
        return blogPostStatisticService.getVisitCountById(blogId, postId);
    }
}

package ch.hsr.sai14sa12.statisticmicroservice.blogpoststatistic;

public interface BlogPostStatisticService {
    int getVisitCountById(long blogId, long postId);
}

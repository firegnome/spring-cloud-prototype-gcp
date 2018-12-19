package ch.hsr.sai14sa12.statisticmicroservice.blogpoststatistic;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;

@Service
public interface BlogPostStatisticProcessor {

    String INPUT = "blogEvents";

    @Input
    SubscribableChannel blogEvents();
}
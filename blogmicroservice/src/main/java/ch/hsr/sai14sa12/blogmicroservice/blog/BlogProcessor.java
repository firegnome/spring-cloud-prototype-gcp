package ch.hsr.sai14sa12.blogmicroservice.blog;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public interface BlogProcessor {

    @Output
    MessageChannel blogEvents();
}

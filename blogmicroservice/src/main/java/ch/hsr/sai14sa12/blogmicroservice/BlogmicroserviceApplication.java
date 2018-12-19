package ch.hsr.sai14sa12.blogmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BlogmicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogmicroserviceApplication.class, args);
    }
}

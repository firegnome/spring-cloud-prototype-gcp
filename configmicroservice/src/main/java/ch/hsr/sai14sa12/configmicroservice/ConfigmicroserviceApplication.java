package ch.hsr.sai14sa12.configmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigmicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigmicroserviceApplication.class, args);
    }
}

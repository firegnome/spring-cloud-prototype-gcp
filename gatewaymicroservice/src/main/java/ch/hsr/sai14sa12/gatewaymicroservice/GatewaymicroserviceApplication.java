package ch.hsr.sai14sa12.gatewaymicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class GatewaymicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewaymicroserviceApplication.class, args);
    }
}

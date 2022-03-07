package com.example.secondservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/second-service")
@Slf4j
public class SecondServiceController {
    Environment env;

    // 주입하기 위해 Autowired 사용하는데, 변수가 아닌 생성자를 통해 주입
    @Autowired
    public SecondServiceController(Environment env){
        this.env=env;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the second service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("second-request") String header){
        log.info(header);
        return "Hello World in Second Service";
    }
    @GetMapping("/check")
    public String check(HttpServletRequest request){
        log.info("Server port={}",request.getServerPort());

        return String.format("It's a message from Second Service on Port %s.",env.getProperty("local.server.port"));
//        return "It's a message from Second Service.";
    }
}

package com.springboot.springboot.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunRestController {

    @Value("${coach.name}")
    private String coachName;

    @Value("${team.name}")
    private String teamName;


    @GetMapping("/")
    public String SayHello() {
        return "Hello World test";
    }

    @GetMapping("/team")
    public String getTeamInfo() {
        return "Team Name: " + teamName + " Coach Name: " + coachName;
    }

}

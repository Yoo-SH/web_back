package com.springCore.springCore.common;
import org.springframework.stereotype.Component;

@Component
public class BaseballCoach implements Coach {

    @Override
    public String getDailyWorkOut() {
        return "Practice 10000 batting practice!";
    }
}

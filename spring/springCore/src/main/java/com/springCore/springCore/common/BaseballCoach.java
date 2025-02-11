package com.springCore.springCore.common;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class BaseballCoach implements Coach {

    public BaseballCoach() {
        System.out.println("BaseballCoach constructor");
    }
    @Override
    public String getDailyWorkOut() {
        return "Practice 10000 batting practice!";
    }
}

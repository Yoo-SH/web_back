package com.springCore.springCore.common;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class CricketCoach implements Coach {

    public CricketCoach() {
        System.out.println("CricketCoach constructor");
    }

    @Override
    public String getDailyWorkOut() {
        return "Practice 10000 step!";
    }
}


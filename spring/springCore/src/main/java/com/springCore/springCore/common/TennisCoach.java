package com.springCore.springCore.common;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Component
@Primary
public class TennisCoach implements Coach {
    @Override
    public String getDailyWorkOut() {
        return "Practice 10000 tennis practice!";
    }
}

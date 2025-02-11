package com.springCore.springCore.common;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Primary
public class TennisCoach implements Coach {
    public TennisCoach() {
        System.out.println("TennisCoach constructor");
    }
    @Override
    public String getDailyWorkOut() {
        return "Practice 10000 tennis practice!";
    }
}

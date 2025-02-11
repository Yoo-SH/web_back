package com.springCore.springCore.rest;

import com.springCore.springCore.common.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private Coach myCoach;
    private Coach mySubCoach;

    @Autowired
    public void setMyCoach(@Qualifier("tennisCoach") Coach theCoach, @Qualifier("tennisCoach") Coach theSubCoach ) {
        myCoach = theCoach;
        mySubCoach = theSubCoach;
    }

    @GetMapping("/practice")
    public String getDailyWorkOut() {
        System.out.println("protoType test: " + (myCoach==mySubCoach));
        return myCoach.getDailyWorkOut();
    }


}


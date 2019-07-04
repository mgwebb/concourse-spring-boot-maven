package com.example;

import org.springframework.web.bind.annotation.RequestMapping;

public class SimpleController {
        @RequestMapping("/")
        public String getMessage() {
            return "-- Welcome to SimpleController New and Improved --";
        }
}

package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
        @RequestMapping("/")
        public String getMessage() {
            return "-- Welcome to SimpleController New and Improved --";
        }
}

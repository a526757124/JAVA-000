package com.billy.starter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
    /**
     *
     * @see
     * @param str
     * @return
     */
    @RequestMapping("/test")
    public  String test(String str){
        return  "Hello";
    }
}

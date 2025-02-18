package com.dw.forest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/forest/index.html")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/forest/singleProduct.html")
    public String singleProduct() {
        return "forward:/singleProduct.html";
    }

    @GetMapping("/forest/course.html")
    public String course() {
        return "forward:/course.html";
    }

    @GetMapping("/forest/notice.html")
    public String notice() {
        return "forward:/notice.html";
    }

    @GetMapping("/forest/qa.html")
    public String qa() {
        return "forward:/qa.html";
    }

    @GetMapping("/forest/mypage.html")
    public String mypage() {
        return "forward:/mypage.html";
    }

    @GetMapping("/forest/login.html")
    public String login() {
        return "forward:/login.html";
    }
}

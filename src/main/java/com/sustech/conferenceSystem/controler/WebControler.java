package com.sustech.conferenceSystem.controler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/")
public class WebControler {

    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }

    @RequestMapping(value = {"/*","/**"})
    public String web(){
        return "hello world";
    }
}

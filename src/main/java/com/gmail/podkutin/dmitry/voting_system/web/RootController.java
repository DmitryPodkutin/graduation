package com.gmail.podkutin.dmitry.voting_system.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:swagger-ui.html";
    }
}

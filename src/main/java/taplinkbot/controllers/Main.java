package taplinkbot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Main {


    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("attrName", "attr value");

        return "index";
    }

}

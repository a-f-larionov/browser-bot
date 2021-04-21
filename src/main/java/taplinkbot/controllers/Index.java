package taplinkbot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class Index {


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public String login(
            @RequestBody LoginForm loginForm,
            HttpSession httpServletRequest
    ) {

        if ("asdf".equals(loginForm.password)) {
            httpServletRequest.setAttribute("isAuthorized", true);
        }

        return "OK";
    }
}

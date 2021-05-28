package taplinkbot.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import taplinkbot.entities.User;

@Controller
public class UserController {

    @GetMapping("/users/get")
    public @ResponseBody
    User getUserProfile() {

        Object object = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (object instanceof User) {
            return (User) object;
        } else {
            return new User();
        }
    }
}

package taplinkbot.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taplinkbot.entities.User;

@RestController
@RequestMapping("/users/")
public class UserController {

    @GetMapping("/get")
    public User getUserProfile() {

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

package taplinkbot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taplinkbot.entities.User;
import taplinkbot.services.UserService;

@RestController
@RequestMapping("/users/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public User getUserProfile() {

        return userService.getCurrentUser();
    }
}

package taplinkbot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import taplinkbot.components.LangComponent;
import taplinkbot.entities.User;
import taplinkbot.service.UserService;

import javax.validation.Valid;

//@todo rest controller
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final LangComponent lang;

    private final UserService userService;

    @GetMapping("/test")
    public @ResponseBody
    String test() {
        return lang.get("label.test");
    }

    @PostMapping("/admin/register-user")
    public ResponseEntity registerUser(@RequestBody @Valid User user) {

        if (userService.registerUser(user)) {
            //@todo messages on one file
            return ResponseEntity.ok("User registered");
        } else {
            return ResponseEntity.ok("Can't register user");
        }
    }
}

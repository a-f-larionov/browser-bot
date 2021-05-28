package taplinkbot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import taplinkbot.entities.User;
import taplinkbot.service.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

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

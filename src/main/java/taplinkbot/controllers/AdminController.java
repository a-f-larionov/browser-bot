//FIN
package taplinkbot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taplinkbot.components.LangComponent;
import taplinkbot.entities.User;
import taplinkbot.services.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/")
public class AdminController {

    private final LangComponent lang;

    private final UserService userService;

    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(@RequestBody @Valid User user) {

        if (userService.registerUser(user)) {
            return ResponseEntity.ok(lang.get("admin.register_user.success"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(lang.get("admin.register_user.failed"));
        }
    }
}

//FIN
package browserbot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import browserbot.services.LangService;
import browserbot.entities.User;
import browserbot.services.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/")
public class AdminController {

    private final LangService lang;

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

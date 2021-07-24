
package browserbot.controllers;

import browserbot.dto.UserDTO;
import browserbot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/")
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @GetMapping("/get")
    public UserDTO get() {

        return modelMapper.map(userService.getCurrentUser(), UserDTO.class);
    }
}

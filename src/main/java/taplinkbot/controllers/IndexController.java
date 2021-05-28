//FIN
package taplinkbot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class IndexController {

    /**
     * Возвращает клиентский код.
     *
     * @return имя представления.
     */
    @GetMapping("/")
    public String index() {
        return "app";
    }
}

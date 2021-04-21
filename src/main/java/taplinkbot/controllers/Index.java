package taplinkbot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class Index {

    @Autowired
    private Environment env;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public String login(
            @RequestBody LoginForm loginForm,
            HttpSession httpSession
    ) {

        String password = env.getProperty("app.userPassword");


        if (password.equals(loginForm.password)) {
            httpSession.setAttribute("isAuthorized", true);
            log.info("set it");
            return "OK!";
        } else {
            log.info("not set it");
            return "NOK";
        }
    }

    @RequestMapping(value = "/is_it_auth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BooleanResponse isItAuth(HttpSession httpSession) {


        //@todo-r refactoring it
        Boolean isAuthorized = (Boolean) httpSession.getAttribute("isAuthorized");
        isAuthorized = isAuthorized != null ? isAuthorized : false;

        if (isAuthorized) {
            return new BooleanResponse(true);
        } else {
            return new BooleanResponse(false);
        }
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "OK!";
    }
}

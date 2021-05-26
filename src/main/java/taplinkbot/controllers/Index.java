package taplinkbot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import taplinkbot.entities.Manager;
import taplinkbot.entities.User;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@Slf4j
public class Index {

    public static final String HTTP_SESSION_ATTR_IS_AUTHORIZED = "isAuthorized";

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @Autowired
    private ManagerRotator managerRotator;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/register-user")
    @ResponseBody
    public String registerUser(@RequestBody @Valid User user) {

        if (userService.registerUser(user)) {
            return "OK";
        } else {
            return "FAILED";
        }
    }

    @PostMapping(value = "/testpost")
    @ResponseBody
    public String testPost() {
        return "test-ok";
    }

    @PostMapping(value = "/authorize2222")
    @ResponseBody
    public String authorize(
            @RequestBody User user,
            HttpSession httpSession
    ) {

        log.info(user.toString());

        /**
         * how to auth?
         */

        String password = env.getProperty("app.userPassword");

        log.info("GUI. is it login " + user.getPassword());

        if (password.equals(user.getPassword())) {
            httpSession.setAttribute(HTTP_SESSION_ATTR_IS_AUTHORIZED, true);
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

        log.info("GUI. is it auth ");

        if (isAuth(httpSession)) {
            return new BooleanResponse(true);
        } else {
            return new BooleanResponse(false);
        }
    }

    @RequestMapping(value = "/get_manager_list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ManagerResponse[] getManagersList(HttpSession httpSession) {

        if (!isAuth(httpSession)) {
            return null;
        }

        log.info("GUI. get_manager_list  ");

        Manager[] source = managerRotator.getManagers();

        ManagerResponse[] response;
        response = new ManagerResponse[source.length];

        for (int i = 0; i < source.length; i++) {
            response[i] = new ManagerResponse();
            response[i].id = source[i].getId();
            response[i].phone = source[i].getPhone();
            response[i].comment = source[i].getComment();
            response[i].isWorking = source[i].isWorking();
        }

        return response;
    }

    @RequestMapping(value = "/manager_working_switch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String managerWorkingSwitch(HttpSession httpSession, @RequestParam int managerId) {

        //@Todo separated file
        log.info("GUI. managerWorkingSwitch " + managerId);

        if (!isAuth(httpSession)) {
            return null;
        }

        managerRotator.isWorkingChange(managerId);
        return "OK";
    }

    private boolean isAuth(HttpSession httpSession) {

        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (object instanceof User) {
            return true;
        } else {
            return false;
        }
    }
}

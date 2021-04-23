package taplinkbot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import taplinkbot.entities.Manager;
import taplinkbot.managers.ManagerRotator;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class Index {


    public static final String HTTP_SESSION_ATTR_IS_AUTHORIZED = "isAuthorized";

    @Autowired
    private Environment env;

    @Autowired
    private ManagerRotator managerRotator;

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

        log.info("GUI. is it login " + loginForm.password);


        if (password.equals(loginForm.password)) {
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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "OK!";
    }

    @RequestMapping(value = "/get_manager_list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ManagerResponse[] getManagersList(HttpSession httpSession) {

        if (!isAuth(httpSession)) {
            return null;
        }

        log.info("GUI. get_manager_list  ");

        Manager[] source = managerRotator.getList();

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
        //@todo-r refactoring it
        Boolean isAuth = (Boolean) httpSession.getAttribute(HTTP_SESSION_ATTR_IS_AUTHORIZED);
        isAuth = isAuth != null ? isAuth : false;
        return isAuth;
    }

}

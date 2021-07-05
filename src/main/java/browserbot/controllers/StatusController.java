package browserbot.controllers;

import browserbot.bots.taplink.Profile;
import browserbot.entities.Setting;
import browserbot.services.Settings;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/status/")
public class StatusController {

    private final Settings settings;

    @RequestMapping("/get")
    public String get() {

        settings.schedulerIsActive(Profile.Canvas);
        JSONObject all = new JSONObject();
        JSONObject canvas = new JSONObject();
        JSONObject ladyart = new JSONObject();
        all.put("canvas", canvas);
        all.put("ladyart", ladyart);

        canvas.put("is_active", settings.schedulerIsActive(Profile.Canvas));
        ladyart.put("is_active", settings.schedulerIsActive(Profile.LadyArt));

        return all.toString();
    }

    @RequestMapping("/run-canvas")
    public void runCanvas() {
        settings.schedulerSetActive(Profile.Canvas, true);
    }

    @RequestMapping("/stop-canvas")
    public void stopCanvas() {
        settings.schedulerSetActive(Profile.Canvas, false);
    }

    @RequestMapping("/run-ladyart")
    public void runLadyArt() {
        settings.schedulerSetActive(Profile.LadyArt, true);
    }

    @RequestMapping("/stop-ladyart")
    public void stopLadyArt() {
        settings.schedulerSetActive(Profile.LadyArt, false);
    }
}

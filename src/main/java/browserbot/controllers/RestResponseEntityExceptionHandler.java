//FIN
package browserbot.controllers;

import browserbot.services.LangService;
import browserbot.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final LangService lang;

    private final TelegramBot telegramBot;

    @ExceptionHandler
    public ResponseEntity<Object> handleConflict(Exception e, WebRequest request) {

        telegramBot.alert(lang.get("application.alert_controller_exception"));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(lang.get("application.exception_handled"));
    }
}

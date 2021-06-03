//FIN
package browserbot.config;

import browserbot.services.LangService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final LangService lang;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException e) throws IOException {

        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(lang.get("authorization.cant_authorize"));
        httpServletResponse.getWriter().flush();
    }
}

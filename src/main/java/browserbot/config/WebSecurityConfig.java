
package browserbot.config;

import browserbot.services.LangService;
import browserbot.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LangService lang;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler(lang);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()

                .antMatchers("/").permitAll()
                .antMatchers("/app.js").permitAll()
                .antMatchers("/main.css").permitAll()

                .antMatchers("/users/get").permitAll()
                .antMatchers("/managers/list").authenticated()

                .antMatchers("/admin/register-user").hasRole("ADMIN")

                .anyRequest().authenticated();

        // Настройка для входа в систему
        http.formLogin((customize) -> {
            customize.loginPage("/");
            customize.loginProcessingUrl("/authorize");
            customize.failureHandler(authenticationFailureHandler());
        });

        // Настройка выхода из системы
        http.logout((customize) -> {
            customize.logoutSuccessUrl("/");
        });
    }

    @Autowired
    protected void configureGlobal(
            AuthenticationManagerBuilder auth,
            UserService userService
    ) throws Exception {

        auth.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder());
    }
}
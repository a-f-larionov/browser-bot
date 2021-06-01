package taplinkbot.config;

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
import taplinkbot.services.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    //private UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // POST Запросы без этого никак @todo доработать post запросы?
        http.csrf().disable();
//
//        //@todo tests: /maangers/list isnt accesing with out auth
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/test").permitAll()

                .antMatchers("/").permitAll()
                .antMatchers("/app.js").permitAll()
                .antMatchers("/main.css").permitAll()

                .antMatchers("/users/get").permitAll()
                .antMatchers("/managers/list").authenticated()

                .antMatchers("/admin/register-user").hasRole("ADMIN")

                .anyRequest().authenticated()
        ;

        //@Todo test authorize
        //@todo migrations for default users
        //Настройка для входа в систему
        http.formLogin((customize) -> {
            customize.loginProcessingUrl("/authorize");
            customize.failureHandler(authenticationFailureHandler());
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
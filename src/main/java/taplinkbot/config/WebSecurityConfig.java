package taplinkbot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import taplinkbot.service.UserService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // POST Запросы без этого никак @todo доработать post запросы?
        http.csrf().disable();

        //Настройка для входа в систему
        http.formLogin()
                .loginPage("/")
                .loginProcessingUrl("/authorize")
                //Перенарпавление на главную страницу после успешного входа
                .defaultSuccessUrl("/?auth=success")
                .permitAll();

        //@todo tests: /maangers/list isnt accesing with out auth
        http.authorizeRequests()

                .antMatchers("/app.js").permitAll()
                .antMatchers("/main.css").permitAll()

                .antMatchers("/users/get").permitAll()
                .antMatchers("/managers/list").authenticated()

                .antMatchers("/admin/register-user").hasRole("ADMIN")

                .anyRequest().authenticated();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder());
    }
}
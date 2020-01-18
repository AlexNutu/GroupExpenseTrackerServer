package ro.unibuc.master.groupexpensetracker.security;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, e) ->
                {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    try {
                        response.getWriter().write(new JSONObject()
                                .put("timestamp", LocalDateTime.now())
                                .put("message", "Access denied")
                                .toString());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                })
                .and()
                .authorizeRequests()
                .antMatchers("/group-expensive-tracker/register").permitAll()
                .antMatchers("/group-expensive-tracker/trip/**").authenticated()
                .antMatchers("/group-expensive-tracker/expense/**").authenticated()
                .antMatchers("/group-expensive-tracker/user/**").authenticated()
                .antMatchers("/group-expensive-tracker/note/**").authenticated()
                .antMatchers("/login").permitAll()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .permitAll()
                .and().csrf().disable();
    }
}
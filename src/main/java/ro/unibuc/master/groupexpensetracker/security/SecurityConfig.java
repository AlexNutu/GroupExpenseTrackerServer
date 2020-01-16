package ro.unibuc.master.groupexpensetracker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//        implements AuthenticationSuccessHandler {

//    private RedirectStrategy redirectStrategy = (httpServletRequest, httpServletResponse, s) -> httpServletResponse.sendRedirect(s);
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response, Authentication authentication) throws IOException {
//        handle(request, response, authentication);
//        clearAuthenticationAttributes(request);
//    }
//
//    private void handle(HttpServletRequest request,
//                        HttpServletResponse response, Authentication authentication) throws IOException {
//        String targetUrl = determineTargetUrl(authentication);
//
//        if (response.isCommitted()) {
//            return;
//        }
//        redirectStrategy.sendRedirect(request, response, targetUrl);
//    }
//
//    private String determineTargetUrl(Authentication authentication) {
//        boolean isUser = false;
//        boolean isAdmin = false;
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        for (GrantedAuthority grantedAuthority : authorities) {
//            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
//                isUser = true;
//                break;
//            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
//                isAdmin = true;
//                break;
//            }
//        }
//
//        if (isUser || isAdmin) {
//            return "redirect:/";
//        } else {
//            throw new IllegalStateException();
//        }
//    }
//
//    private void clearAuthenticationAttributes(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            return;
//        }
//        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                .antMatchers("/css/**", "/index").permitAll()
//                .antMatchers("/group-expensive-tracker/trip/**").permitAll()
                .antMatchers("/group-expensive-tracker/trip/**").authenticated()
                .antMatchers("/group-expensive-tracker/user/**").permitAll()
                .antMatchers("/login").permitAll()
//                .antMatchers("/group-expensive-tracker/user/**").hasRole("USER");
                .and()
                .formLogin()
//                .loginPage("/")
                .loginProcessingUrl("/login")
                .permitAll()
                .and().csrf().disable();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("email").password("password").roles("USER");
//    }
//
//    @Autowired
//    private GroupExpenseAuthenticationProvider provider;
//
//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint() {
//        return new LoginUrlAuthenticationEntryPoint("/login");
//    }
//
////    @Bean
////    public OpenIDConnectAuthenticationFilter openIdConnectAuthenticationFilter() {
////        OpenIDConnectAuthenticationFilter filter = new OpenIDConnectAuthenticationFilter(GOOGLE_LOGIN_ENTRY_POINT);
////        filter.setAuthenticationSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> httpServletResponse.sendRedirect("/success"));
////        return filter;
////    }
//
//    @Bean
//    public OAuth2ClientContextFilter oAuth2ClientContextFilter() {
//        OAuth2ClientContextFilter filter = new OAuth2ClientContextFilter();
//        filter.setRedirectStrategy(redirectStrategy);
//        return filter;
//    }
}
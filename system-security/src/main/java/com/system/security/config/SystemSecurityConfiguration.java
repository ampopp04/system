package com.system.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The <class>SystemSecurityConfiguration</class> defines
 * the default configuration for this project
 *
 * @author Andrew
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SystemSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * The user detail service used for security verification
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configure the default security for this project
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/console/**").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/index.html")
                .loginProcessingUrl("/login")
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession();
                    UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    session.setAttribute("username", authUser.getUsername());
                    session.setAttribute("authorities", authentication.getAuthorities());

                    //set our response to OK status
                    response.setStatus(HttpServletResponse.SC_OK);
                }).and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and().headers().frameOptions().disable()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .deleteCookies("remember-me")
                .permitAll().and()
                .rememberMe();
    }

    /**
     * Configure to use our user detail service with hashed/salted passwords
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
}
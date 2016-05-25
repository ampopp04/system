package com.system.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

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
public class SystemSecurityConfiguration extends WebSecurityConfigurerAdapter {

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
                    User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    session.setAttribute("username", authUser.getUsername());
                    session.setAttribute("authorities", authentication.getAuthorities());

                    //set our response to OK status
                    response.setStatus(HttpServletResponse.SC_OK);
                }).and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and().headers().frameOptions().disable().and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();
    }

    /**
     * Configure the global  authentication settings
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("apopp").password("password").roles("USER");
    }
}
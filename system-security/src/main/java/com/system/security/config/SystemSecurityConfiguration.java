/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.security.config;

import com.system.security.user.SystemSecurityUserRepository;
import com.system.security.user.detail.mapper.SystemSecurityUserDetailsContextMapper;
import com.system.security.user.detail.provider.SystemSecurityUserDetailActiveDirectoryProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.StaticResourceRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.CacheControlHeadersWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>SystemSecurityConfiguration</class> defines
 * the default configuration for this project
 *
 * @author Andrew
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(SecurityProperties.BASIC_AUTH_ORDER - 1)
@PropertySource(value = {"classpath:application-system-security.properties"})
@PropertySource(value = {"classpath:application-system-security-active-directory.properties"}, ignoreResourceNotFound = true)
public class SystemSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Active directory manager distinguished name
     */
    @Value("${ad.manager.dn:#{null}}")
    private String AD_MANAGER_DN;

    /**
     * Active directory manager password
     */
    @Value("${ad.manager.password:#{null}}")
    private String AD_MANAGER_PASSWORD;

    /**
     * Active directory url
     */
    @Value("${ad.url:#{null}}")
    private String AD_URL;

    /**
     * The user detail service used for security verification
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * The user repository service used for security user retrieval
     */
    @Autowired
    private SystemSecurityUserRepository systemSecurityUserRepository;

    @Bean
    public HeaderWriter selectiveCacheControlHeaderWriter() {
        RequestMatcher paths = new OrRequestMatcher(new AntPathRequestMatcher("/api/**"), new AntPathRequestMatcher("/ajax/**"));
        return new DelegatingRequestMatcherHeaderWriter(paths, new CacheControlHeadersWriter());
    }

    /**
     * Configure the default security for this project
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //    .requiresChannel().anyRequest().requiresSecure().and()
                .authorizeRequests()

                //  .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .requestMatchers(StaticResourceRequest.toCommonLocations()).permitAll()
                .antMatchers("/console/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/*").permitAll().antMatchers("/resources/**").permitAll()
                .antMatchers("/**").permitAll()
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
                .cacheControl().disable().addHeaderWriter(selectiveCacheControlHeaderWriter())
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .deleteCookies("remember-me")
                .permitAll().and()
                .rememberMe().alwaysRemember(false);
    }

    /**
     * Configure to use our user detail service with hashed/salted passwords
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authenticationManager());
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(encoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        if (AD_URL != null) {
            return new ProviderManager(asList(daoAuthenticationProvider(), activeDirectoryLdapAuthenticationProvider()));
        }

        return new ProviderManager(asList(daoAuthenticationProvider()));
    }


    @Bean
    @ConditionalOnProperty(name = "ad.url")
    public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        logger.warn("Enabling Active Directory Ldap Authentication Provider with url=[" + AD_URL + "], manager dn=[" + AD_MANAGER_DN + "], password=[PROTECTED]");

        SystemSecurityUserDetailActiveDirectoryProvider provider = new SystemSecurityUserDetailActiveDirectoryProvider(AD_URL, AD_MANAGER_DN, AD_MANAGER_PASSWORD, systemSecurityUserRepository);

        provider.setUseAuthenticationRequestCredentials(true);
        provider.setUserDetailsContextMapper(systemSecurityUserDetailsLdapContextMapper(userDetailsService()));

        return provider;
    }

    @Bean
    @ConditionalOnMissingBean
    public LdapUserDetailsMapper systemSecurityUserDetailsLdapContextMapper(UserDetailsService userDetailsService) {
        return new SystemSecurityUserDetailsContextMapper(userDetailsService);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
}
package com.utp.creacionesjoaquin.security;

import com.utp.creacionesjoaquin.security.jwt.JwtEntryPoint;
import com.utp.creacionesjoaquin.security.jwt.JwtTokenFilter;
import com.utp.creacionesjoaquin.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MainSecurity {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    private AuthenticationManager authenticationManager;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        AuthenticationManagerBuilder builder = http.getSharedObject( AuthenticationManagerBuilder.class );
        builder.userDetailsService( userDetailsService ).passwordEncoder(passwordEncoder);
        authenticationManager = builder.build();

        http.authenticationManager( authenticationManager );
        http.authenticationProvider( authenticationProvider() );

        return http.authorizeHttpRequests( registry -> {
            registry.requestMatchers(HttpMethod.POST).permitAll();
            registry.requestMatchers(HttpMethod.GET).permitAll();
            registry.requestMatchers("/api/category/public/**").permitAll();
            registry.requestMatchers("/api/product/public/**").permitAll();
            registry.requestMatchers("/api/category/public/**").permitAll();
            registry.requestMatchers("/api/sub-category/public/**").permitAll();
            registry.requestMatchers("/api/collection/public/**").permitAll();
            registry.requestMatchers("/api/auth/**").permitAll();
            registry.anyRequest().authenticated();
        }).exceptionHandling( httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint( jwtEntryPoint )
        ).addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class).build();

    }

}


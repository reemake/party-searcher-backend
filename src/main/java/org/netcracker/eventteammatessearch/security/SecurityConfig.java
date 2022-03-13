package org.netcracker.eventteammatessearch.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private JwtAuthProvider jwtAuthProvider;
    private JWTFilter jwtFilter;
    private UsernamePasswordAuthProvider usernamePasswordAuthProvider;
    private UsernamePasswordFilter usernamePasswordFilter;

    public SecurityConfig(JwtAuthProvider jwtAuthProvider, @Lazy JWTFilter jwtFilter, UsernamePasswordAuthProvider usernamePasswordAuthProvider, UsernamePasswordFilter usernamePasswordFilter) {
        this.jwtAuthProvider = jwtAuthProvider;
        this.jwtFilter = jwtFilter;
        this.usernamePasswordAuthProvider = usernamePasswordAuthProvider;
        this.usernamePasswordFilter = usernamePasswordFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .cors().disable();
        http.authenticationProvider(jwtAuthProvider)
                .authenticationProvider(usernamePasswordAuthProvider)
                .authorizeRequests()
                .anyRequest()
                .authenticated();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(usernamePasswordFilter, JWTFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

package org.netcracker.eventteammatessearch.security;

import org.netcracker.eventteammatessearch.security.Filters.JWTFilter;
import org.netcracker.eventteammatessearch.security.Filters.UsernamePasswordFilter;
import org.netcracker.eventteammatessearch.security.OAuth.OAuthSuccessHandler;
import org.netcracker.eventteammatessearch.security.Providers.JwtAuthProvider;
import org.netcracker.eventteammatessearch.security.Providers.UsernamePasswordAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity()
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${FRONTEND}")
    private String frontendAddress;
    private JwtAuthProvider jwtAuthProvider;
    private JWTFilter jwtFilter;
    private UsernamePasswordAuthProvider usernamePasswordAuthProvider;
    private UsernamePasswordFilter usernamePasswordFilter;

    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler;

    public SecurityConfig(@Lazy JwtAuthProvider jwtAuthProvider, @Lazy JWTFilter jwtFilter, @Lazy UsernamePasswordAuthProvider usernamePasswordAuthProvider, @Lazy UsernamePasswordFilter usernamePasswordFilter) {
        this.jwtAuthProvider = jwtAuthProvider;
        this.jwtFilter = jwtFilter;
        this.usernamePasswordAuthProvider = usernamePasswordAuthProvider;
        this.usernamePasswordFilter = usernamePasswordFilter;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/chatService/**", "/ws/**");
    }

    @Bean
    public RequestMatcher requestMatcher() {
        return new MvcRequestMatcher(new HandlerMappingIntrospector(), "/login");
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                cors().and().
                httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/register", "/refreshToken", "/api/events/getEventsWithinRadius", "/api/events/filter", "/api/eventTypes/**", "/error/**", "/oauth2/**", "/login**", "/generateRefreshToken")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login().successHandler(oAuthSuccessHandler).and()
                .addFilterAt(
                        usernamePasswordFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(usernamePasswordAuthProvider)
                .authenticationProvider(jwtAuthProvider);


    }



    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(frontendAddress);
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setExposedHeaders(Arrays.asList("refreshToken", "token"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


}

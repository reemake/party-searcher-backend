package org.netcracker.eventteammatessearch.security.Providers;

import org.netcracker.eventteammatessearch.security.Entity.JWTAuthentication;
import org.netcracker.eventteammatessearch.security.Persistence.Entity.JWTUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthProvider implements AuthenticationProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getCredentials() != null) {
            JWTUser jwtUser = new JWTUser(authentication.getCredentials().toString(), secretKey);
            if (jwtUser.isAccountNonExpired()) {
                authentication.setAuthenticated(true);
                return authentication;
            }

        }
        throw new BadCredentialsException("YOUR token is empty or invalid");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JWTAuthentication.class.isAssignableFrom(aClass);
    }
}

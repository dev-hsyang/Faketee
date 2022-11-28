package com.konai.kurong.faketee.util;

import com.konai.kurong.faketee.config.auth.PrincipalDetails;
import com.konai.kurong.faketee.config.auth.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PrincipalDetailsService principalDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username);

        if(!bCryptPasswordEncoder.matches(password, principalDetails.getPassword())){

            throw new BadCredentialsException(username);
        }
        if(!principalDetails.isEnabled()){

            throw new BadCredentialsException(username);
        }

        return new UsernamePasswordAuthenticationToken(username, password, principalDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return true;
    }

    public void setbCryptPasswordEncoder(BCryptPasswordEncoder encoder) {

        this.bCryptPasswordEncoder = encoder;
    }
}

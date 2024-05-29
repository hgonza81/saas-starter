package com.buteler.saasstarter.security;

import com.buteler.saasstarter.security.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${com.buteler.security.auth-url-prefix}")
//@CrossOrigin
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("${com.buteler.security.login-endpoint}")
    public Map<Object, Object> login(@RequestBody @Valid AuthenticationRequest data) throws BadCredentialsException {
        Map<Object, Object> response = new HashMap<>();
        var authFromRequest = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        var authFromAuthManager = authenticationManager.authenticate(authFromRequest);
        String jwtToken = jwtTokenProvider.createToken(authFromAuthManager);
        response.put("jwt", jwtToken);
        return response;
    }

    @PostMapping("${com.buteler.security.logout-endpoint}")
    public Map<Object, Object> login() throws BadCredentialsException {
        Map<Object, Object> response = new HashMap<>();
        SecurityContextHolder.clearContext();
        response.put("message", "Logout successful");
        return response;
    }

}
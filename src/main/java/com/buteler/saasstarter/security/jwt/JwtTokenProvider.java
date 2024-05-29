package com.buteler.saasstarter.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Utility class to create and validate JWT tokens
 */
@Component
@Slf4j
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "roles";

    private final JwtProperties jwtProperties;

    private final SecretKey secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        var secret = Base64.getEncoder().encodeToString(this.jwtProperties.getSecretKey().getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Create a JWT token from a complete Authentication object
     *
     * @param authentication is the Authentication object with principal, credentials and authorities
     * @return a valid JWT token string
     */
    public String createToken(Authentication authentication) {
        // Add the subject to the claims
        String username = authentication.getName();
        JwtBuilder tokenBuilder = Jwts.builder().subject(username);

        // Add the authorities to the claims
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (!authorities.isEmpty()) {
            String authoritiesString = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            tokenBuilder.claim(AUTHORITIES_KEY, authoritiesString);
        }

        // Add the issuedAt and expiration date to the claims
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.jwtProperties.getValidityInMs());
        tokenBuilder
                .issuedAt(now)
                .expiration(validity);

        // Sign and return the token string
        String token = tokenBuilder
                .signWith(this.secretKey, Jwts.SIG.HS512)
                .compact();

        return token;
    }

    /**
     * Parse a valid JWT token and return an Authentication object
     *
     * @param token is the JWT token
     * @return the associated Authentication object
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ?
                AuthorityUtils.NO_AUTHORITIES :
                AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * Validate a JWT token
     *
     * @param token is the JWT token
     * @return true if the token is valid and haven´t expired, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(this.secretKey)
                    .build()
                    .parseSignedClaims(token);
            //  parseSignedClaims will check the expiration date. No need to do it here.
            log.debug("Valid JWT with expiration date: {}", claims.getPayload().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }

        return false;
    }

}
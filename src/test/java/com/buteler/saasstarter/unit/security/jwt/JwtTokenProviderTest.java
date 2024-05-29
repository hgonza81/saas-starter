package com.buteler.saasstarter.unit.security.jwt;

import com.buteler.saasstarter.security.jwt.JwtProperties;
import com.buteler.saasstarter.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    JwtProperties jwtProperties = new JwtProperties();

    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtProperties.setSecretKey(
                "RZEneMII/RQyX4hxuGh32UdDSaaLRtghnxQnk+M8afIHiviXAFg+NfXCHgkPv5IGdUNuq3O3lQRpP5bnR/roJg==");
        jwtProperties.setValidityInMs(3600000);
        jwtTokenProvider = new JwtTokenProvider(jwtProperties);
    }

    @Test
    void shouldReturnValidToken_whenValidAuthenticationIsProvided() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("hernan", "password",
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        // When
        String token = jwtTokenProvider.createToken(authentication);
        // Then
        assertThat(token)
                .as("Check if the token is not null")
                .isNotNull();
        assertThat(token)
                .as("Check if the token is not empty")
                .isNotEmpty();
    }

    @Test
    void shouldReturnValidAuthenticationObject_whenValidTokenIsProvided() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("hernan", "password",
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        String validToken = jwtTokenProvider.createToken(authentication);
        // When
        Authentication authenticationFromToken = jwtTokenProvider.getAuthentication(validToken);
        // Then
        assertThat(authenticationFromToken)
                .as("Check if the authentication is not null")
                .isNotNull();
        assertThat(authenticationFromToken.getName())
                .as("Check if the authentication has the correct username")
                .isEqualTo("hernan");

        assertThat(authenticationFromToken.getAuthorities())
                .as("Check if the authentication has the correct authorities")
                .isEqualTo(AuthorityUtils.createAuthorityList("ROLE_USER"));
    }

    @Test
    void shouldReturnTrue_whenValidTokenIsProvided() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("hernan", "password",
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        String validToken = jwtTokenProvider.createToken(authentication);
        // When
        boolean tokenIsValid = jwtTokenProvider.validateToken(validToken);
        // Then
        assertThat(tokenIsValid)
                .as("Check if the token is valid. Token: %s ", validToken)
                .isTrue();
    }

    @Test
    void shouldReturnFalse_whenInvalidTokenIsProvided() {
        // Given
        String invalidToken = "invalid-token";
        // When
        boolean tokenIsValid = jwtTokenProvider.validateToken(invalidToken);
        // Then
        assertThat(tokenIsValid)
                .as("Check if the token is valid. Token: %s ", invalidToken)
                .isFalse();
    }

    @Test
    void shouldReturnFalse_whenExpiredTokenIsProvided() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("hernan", "password",
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        jwtProperties.setValidityInMs(0); // We want the token to expire immediately
        String validToken = jwtTokenProvider.createToken(authentication);

        // When
        boolean tokenIsValid = jwtTokenProvider.validateToken(validToken);

        // Then
        assertThat(tokenIsValid)
                .as("Check if the token is not expired. Token: %s ", validToken)
                .isFalse();
    }

    @Test
    void shouldReturnFalse_whenTokenSignedWithAnotherKeyIsProvided() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("hernan", "password",
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        String validToken = jwtTokenProvider.createToken(authentication); // Sign with the default key
        // Set a different key and create a new JwtTokenProvider so it reads the new key
        jwtProperties.setSecretKey(
                "Gih3GxcBir4vMRCJ/eahbLGrfULiww0OhG7T81DaKP5Roi8b/e4QKhlt9vFvQGEfoKj6sjArYSyoKFqiar4hlw==");
        jwtTokenProvider = new JwtTokenProvider(jwtProperties);

        // When
        boolean tokenIsValid = jwtTokenProvider.validateToken(validToken);

        // Then
        assertThat(tokenIsValid)
                .as("Check if the token is signed with valid Key. Token: %s ", validToken)
                .isFalse();
    }

}
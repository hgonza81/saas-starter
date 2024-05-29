package com.buteler.saasstarter.integration.security;

import com.buteler.saasstarter.integration.BaseIntegrationTest;
import com.buteler.saasstarter.security.SecurityProperties;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.notNullValue;

class AuthenticationControllerIT extends BaseIntegrationTest {

    @Autowired
    SecurityProperties securityProperties;

    String jwtToken;

    @Test
    @Order(1)
    public void shouldNotReturnJwtToken_whenLoginEndpointIsCalledWithInvalidCredentials() throws JSONException {
        // Given
        JSONObject loginCredentials = new JSONObject();
        loginCredentials.put("username", "hernan.gonzalez81@gmail.com");
        loginCredentials.put("password", "invalidCredentials");
        // When
        with().body(loginCredentials.toString()).contentType(MediaType.APPLICATION_JSON_VALUE).when().post(securityProperties.getFullLoginUrl()).then().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Order(2)
    public void shouldLogin_whenLoginEndpointIsCalledWithValidCredentials() throws JSONException {
        // Given
        JSONObject loginCredentials = new JSONObject();
        loginCredentials.put("username", "hernan.gonzalez81@gmail.com");
        loginCredentials.put("password", "123456");
        // When
        Response response = with().body(loginCredentials.toString()).contentType(MediaType.APPLICATION_JSON_VALUE).when().post(securityProperties.getFullLoginUrl());
        // Then
        response.then().statusCode(HttpStatus.OK.value()).body("jwt", notNullValue());

        jwtToken = response.jsonPath().getString("jwt");
    }

    @Test
    @Order(3)
    public void shouldLogout_whenLogoutEndpointIsCalled() throws JSONException {
        with().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).when().post(securityProperties.getFullLogout()).then().statusCode(HttpStatus.OK.value()).body("message", org.hamcrest.Matchers.equalTo("Logout successful"));
    }
}
package com.buteler.saasstarter.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.buteler.security.jwt")
@Getter
@Setter
public class JwtProperties {

    private String secretKey;

    private long validityInMs; // validity in milliseconds

}
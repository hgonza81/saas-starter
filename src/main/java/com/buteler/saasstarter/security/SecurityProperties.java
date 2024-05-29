package com.buteler.saasstarter.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.buteler.security")
@Getter
@Setter
public class SecurityProperties {

    @Getter(AccessLevel.PRIVATE)
    private String loginEndpoint; // use getFullLoginUrl() instead

    @Getter(AccessLevel.PRIVATE)
    private String logoutEndpoint; // use getFullLogout() instead

    private String apiUrlPrefix;
    private String authUrlPrefix;
    private String corsAllowedOrigin;
    private String swaggerUiUrl;
    private String swaggerApiDocsUrl;

    public String getFullLoginUrl() {
        return authUrlPrefix + loginEndpoint;
    }

    public String getFullLogout() {
        return authUrlPrefix + logoutEndpoint;
    }

}

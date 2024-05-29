package com.buteler.saasstarter.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.buteler")
@Getter
@Setter
public class UserProperties {

    private String apiUrlPrefix;
    private String userApiEndpoint;

    public String getFullApiUserEndpoint() {
        return apiUrlPrefix + userApiEndpoint;
    }

}
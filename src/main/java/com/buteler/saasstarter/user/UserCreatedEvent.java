package com.buteler.saasstarter.user;

import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;

public class UserCreatedEvent extends ApplicationEvent {

    private final String username;

    public UserCreatedEvent(Object source, String username) {
        super(source);

        Assert.notNull(username, "username must not be null");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
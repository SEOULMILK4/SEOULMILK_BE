package com.seoulmilk.seoulmilkServer.global.mail.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private final String host;
    private final String port;
    private final String username;
    private final String password;


}

package com.seoulmilk.seoulmilkServer.global.mail.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "mail")
public class EmailProperties {

    private String host;
    private String username;
    private String password;

}

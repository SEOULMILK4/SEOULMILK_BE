package com.seoulmilk.seoulmilkServer.global.etc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "member")
@Getter
@Setter
public class MemberProperties {

    private String defaultPassword;

}

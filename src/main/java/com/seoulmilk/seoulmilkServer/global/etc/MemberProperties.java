package com.seoulmilk.seoulmilkServer.global.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "member")
public class MemberProperties {

    private String defaultPassword;

}

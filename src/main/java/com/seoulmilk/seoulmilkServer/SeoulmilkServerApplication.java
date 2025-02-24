package com.seoulmilk.seoulmilkServer;

import com.seoulmilk.seoulmilkServer.global.jwt.properties.JwtProperties;
import com.seoulmilk.seoulmilkServer.global.mail.properties.MailProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, MailProperties.class})
@EnableJpaAuditing
public class SeoulmilkServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeoulmilkServerApplication.class, args);
	}

}

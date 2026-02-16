package com.github.atzhukov.sbmtdemo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfiguration {

	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.csrf { it.disable() }
			.authorizeHttpRequests { auth -> auth
				.requestMatchers("/**").permitAll() // FIXME
				.anyRequest().authenticated()
			}
			.httpBasic {}
		return http.build()
	}

}

package com.github.atzhukov.sbmtdemo.config

import com.github.atzhukov.sbmtdemo.config.auth.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

	@Bean
	fun securityFilterChain(
		http: HttpSecurity,
		jwtAuthFilter: JwtAuthFilter
	): SecurityFilterChain {
		http
			.csrf { it.disable() }
			.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
			.authorizeHttpRequests { auth ->
				auth
					.requestMatchers(HttpMethod.POST, "/api/signup").permitAll()
					.requestMatchers(HttpMethod.POST, "/api/signin").permitAll()
					.anyRequest().authenticated()
			}
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
		return http.build()
	}

	@Bean
	fun authenticationManager(
		userDetailsService: UserDetailsService,
		passwordEncoder: PasswordEncoder
	): AuthenticationManager {
		val authenticationProvider = DaoAuthenticationProvider(userDetailsService)
		authenticationProvider.setPasswordEncoder(passwordEncoder)
		return ProviderManager(authenticationProvider)
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder()
	}

}

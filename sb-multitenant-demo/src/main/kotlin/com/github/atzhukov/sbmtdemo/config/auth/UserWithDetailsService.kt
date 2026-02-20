package com.github.atzhukov.sbmtdemo.config.auth

import com.github.atzhukov.sbmtdemo.service.AuthService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserWithDetailsService(
	private val authService: AuthService,
): UserDetailsService {
	override fun loadUserByUsername(username: String): UserDetails {
		val user = authService.getByLogin(username) ?: throw UsernameNotFoundException("Wrong username")
		return UserWithDetails(user)
	}
}

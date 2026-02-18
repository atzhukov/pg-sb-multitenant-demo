package com.github.atzhukov.sbmtdemo.config.auth

import com.github.atzhukov.sbmtdemo.data.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserWithDetails(
	val user: User
): UserDetails {
	override fun getUsername(): String = user.login!!
	override fun getPassword(): String? = user.password
	override fun getAuthorities(): Collection<GrantedAuthority> = emptySet()
}

internal fun User.asUserDetails(): UserDetails = UserWithDetails(this)

package com.github.atzhukov.sbmtdemo.config.auth

import org.springframework.security.core.context.SecurityContextHolder

object CurrentUser {
	private val auth: JwtAuthentication?
		get() = SecurityContextHolder.getContext().authentication as? JwtAuthentication

	val id: Long?
		get() = auth?.userId

	val login: String?
		get() = auth?.principal

	val tenantIds: List<Long>
		get() = auth?.tenantIds ?: emptyList()
}

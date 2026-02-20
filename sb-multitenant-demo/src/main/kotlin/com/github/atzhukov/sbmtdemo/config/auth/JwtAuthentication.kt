package com.github.atzhukov.sbmtdemo.config.auth

import org.springframework.security.authentication.AbstractAuthenticationToken

class JwtAuthentication(
	val userId: Long,
	val username: String,
	val tenantIds: List<Long>
): AbstractAuthenticationToken(emptyList()) {

	override fun getPrincipal() = username
	override fun getCredentials() = null

}

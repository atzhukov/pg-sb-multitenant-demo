package com.github.atzhukov.sbmtdemo.config.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtService(
	@Value($$"${server.jwt.secret-key}")
	private val secret: String
) {

	companion object {
		const val EXPIRATION_MS = 60 * 60 * 1000
	}

	private val secretKey: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

	fun createToken(user: UserWithDetails): String = Jwts.builder()
		.subject(user.username)
		.claim("sub-id", user.id)
		.claim("ten-id", user.tenantIds)
		.issuedAt(Date())
		.expiration(Date(System.currentTimeMillis() + EXPIRATION_MS))
		.signWith(secretKey)
		.compact()

	fun parseToken(token: String): JwtAuthentication {
		val claims = Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)

		return JwtAuthentication(
			userId = (claims.payload["sub-id"] as Number).toLong(),
			username = claims.payload.subject,
			tenantIds = claims.payload["ten-id"] as List<Long>,
		).also { it.isAuthenticated = true }
	}

}

package com.github.atzhukov.sbmtdemo.config.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtService(
	@Value($$"${server.jwt.secret}")
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

	fun parseToken(token: String, request: HttpServletRequest? = null): JwtAuthentication {
		val claims = Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)

		val auth = JwtAuthentication(
			userId = (claims.payload["sub-id"] as Number).toLong(),
			username = claims.payload.subject,
			tenantIds = claims.payload["ten-id"] as List<Long>,
		).also { it.isAuthenticated = true }

		if (request != null) {
			auth.details = WebAuthenticationDetailsSource().buildDetails(request)
		}
		return auth
	}

}

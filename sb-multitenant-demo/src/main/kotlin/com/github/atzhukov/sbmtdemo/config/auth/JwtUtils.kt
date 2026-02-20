package com.github.atzhukov.sbmtdemo.config.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtils(
	@Value($$"${server.jwt.secret-key}")
	private val secret: String
) {

	companion object {
		const val EXPIRATION_MS = 60 * 60 * 1000
	}

	private val secretKey: SecretKey = Keys.hmacShaKeyFor(
		secret.toByteArray()
	)

	fun createToken(user: UserWithDetails): String = Jwts.builder()
		.subject(user.username)
		.claim("sub_id", user.id)
		.claim("ten_id", user.tenantIds)
		.issuedAt(Date())
		.expiration(Date(System.currentTimeMillis() + EXPIRATION_MS))
		.signWith(secretKey)
		.compact()

}

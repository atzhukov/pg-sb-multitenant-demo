package com.github.atzhukov.sbmtdemo.config.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Date

class Jwt {

	companion object {
		const val SECRET_KEY = "changeitchangeitchangeitchangeitchangeitchangeit"
		const val EXPIRATION_MS = 60 * 60 * 1000

		fun of(login: String) = Jwts.builder()
			.subject(login)
			.claim("tenants", listOf(1)) // FIXME
			.issuedAt(Date())
			.expiration(Date(System.currentTimeMillis() + EXPIRATION_MS))
			.signWith(Keys.hmacShaKeyFor(SECRET_KEY.toByteArray()))
			.compact()
	}

}

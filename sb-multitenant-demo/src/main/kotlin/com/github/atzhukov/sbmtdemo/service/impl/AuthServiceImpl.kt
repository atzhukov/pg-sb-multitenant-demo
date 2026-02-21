package com.github.atzhukov.sbmtdemo.service.impl

import com.github.atzhukov.sbmtdemo.config.auth.JwtService
import com.github.atzhukov.sbmtdemo.config.auth.UserWithDetails
import com.github.atzhukov.sbmtdemo.data.entity.Tenant
import com.github.atzhukov.sbmtdemo.data.entity.User
import com.github.atzhukov.sbmtdemo.service.AuthService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Lazy
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.queryForObject
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.sql.ResultSet
import java.time.OffsetDateTime

@Service
class AuthServiceImpl(
	@Qualifier("authJdbcTemplate")
	private val jdbcTemplate: JdbcTemplate,
	@Qualifier("authTransactionTemplate")
	private val transactionTemplate: TransactionTemplate,
	@Lazy
	private val authenticationManager: AuthenticationManager,
	private val passwordEncoder: PasswordEncoder,
	private val jwtService: JwtService
): AuthService {

	companion object {
		const val SQL_USER_EXISTS = "SELECT EXISTS (SELECT 1 FROM users WHERE login = ?)"
		const val SQL_USER = """
			SELECT
				u.*,
				t.id AS tenant_id,
				t.name AS tenant_name
			FROM users u
				LEFT JOIN users_to_tenants utt ON utt.user = u.id
				LEFT JOIN tenants t ON t.id = utt.tenant
			WHERE u.login = ?
			"""
		const val SQL_NEW_USER = "INSERT INTO users (login, password, name) VALUES (?, ?, ?) RETURNING id"
		const val SQL_NEW_USER_TENANTS = "INSERT INTO users_to_tenants (\"user\", tenant) VALUES (?, ?)"
	}

	override fun existsByLogin(login: String): Boolean {
		return jdbcTemplate.queryForObject(SQL_USER_EXISTS, login) ?: false
	}

	override fun getByLogin(login: String): User? {
		return jdbcTemplate.query(SQL_USER, ResultSetExtractor(::extractUserWithTenants), login)
	}

	override fun createUser(user: User): Long {
		if (existsByLogin(user.login)) {
			throw IllegalArgumentException("User with this login already exists")
		}

		val password = passwordEncoder.encode(user.password)
		return transactionTemplate.execute tx@ {
			val userId = jdbcTemplate.queryForObject<Long>(SQL_NEW_USER, user.login, password, user.name)
				?: throw IllegalStateException("No user ID was returned")
			if (user.tenants.isNotEmpty()) {
				val parameters = user.tenants.map { arrayOf(userId, it.id!!) }
				jdbcTemplate.batchUpdate(SQL_NEW_USER_TENANTS, parameters)
			}
			return@tx userId
		}
	}

	override fun signIn(login: String, password: String): String {
		val credentialsToken = UsernamePasswordAuthenticationToken(
			login, password
		)
		val auth = authenticationManager.authenticate(credentialsToken)
		val principal = auth.principal as UserWithDetails
		return jwtService.createToken(principal)
	}

	private fun extractUserWithTenants(rs: ResultSet): User? {
		var user: User? = null
		val tenants = mutableSetOf<Tenant>()

		while (rs.next()) {
			if (user == null) {
				user = User(
					id = rs.getLong("id"),
					login = rs.getString("login"),
					password = rs.getString("password"),
					name = rs.getString("name"),
					lastLogin = rs.getObject("last_login", OffsetDateTime::class.java),
					tenants = tenants
				)
			}
			if (rs.getLong("tenant_id") == 0L) {
				continue
			}
			tenants.add(
				Tenant(
					id = rs.getLong("tenant_id"),
					name = rs.getString("tenant_name")
				)
			)
		}

		return user
	}

}

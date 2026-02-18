package com.github.atzhukov.sbmtdemo.service.impl

import com.github.atzhukov.sbmtdemo.data.entity.Tenant
import com.github.atzhukov.sbmtdemo.data.entity.User
import com.github.atzhukov.sbmtdemo.service.AuthService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.queryForObject
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.time.OffsetDateTime

@Service
class AuthServiceImpl(
	@Qualifier("authJdbcTemplate")
	private val jdbcTemplate: JdbcTemplate,
	private val passwordEncoder: PasswordEncoder,
): AuthService {

	override fun existsByLogin(login: String): Boolean {
		val sql = "SELECT EXISTS (SELECT 1 FROM users WHERE login = ?)"
		return jdbcTemplate.queryForObject(sql, login) ?: false
	}

	override fun getByLogin(login: String): User? {
		val sql = """
			SELECT
				u.*,
				t.id AS tenant_id,
				t.name AS tenant_name
			FROM users u
				LEFT JOIN users_to_tenants utt ON utt.user = u.id
				LEFT JOIN tenants t ON t.id = utt.tenant
			WHERE u.login = ?
			""".trimIndent()
		return jdbcTemplate.query(sql, ResultSetExtractor(::extractUserWithTenants), login)
	}

	@Transactional
	override fun create(user: User, password: String) {
		if (existsByLogin(user.login!!)) {
			throw IllegalArgumentException("User with this login already exists")
		}
		val sql = "INSERT INTO users (login, password, name) VALUES (?, ?, ?)"
		jdbcTemplate.update(sql, user.login!!, passwordEncoder.encode(password), user.login!! + "@@@")
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

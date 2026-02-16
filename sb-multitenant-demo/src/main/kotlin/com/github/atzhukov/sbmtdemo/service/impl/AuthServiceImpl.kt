package com.github.atzhukov.sbmtdemo.service.impl

import com.github.atzhukov.sbmtdemo.data.entity.Tenant
import com.github.atzhukov.sbmtdemo.data.entity.User
import com.github.atzhukov.sbmtdemo.service.AuthService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.time.OffsetDateTime

@Service
class AuthServiceImpl(
    @Qualifier("authJdbcTemplate")
    private val jdbcTemplate: JdbcTemplate,
): AuthService {

    override fun getByLogin(login: String): User? {
        val sql = """
            SELECT
                u.*,
                t.id AS tenant_id,
                t.name AS tenant_name
            FROM users u
                JOIN users_to_tenants utt ON utt.user = u.id
                JOIN tenants t ON t.id = utt.tenant
            WHERE u.login = ?
            """.trimIndent()
        return jdbcTemplate.query(sql, ResultSetExtractor(::extractUserWithTenants), login)
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
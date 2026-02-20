package com.github.atzhukov.sbmtdemo.data

import com.github.atzhukov.sbmtdemo.config.auth.JwtAuthentication
import org.springframework.jdbc.datasource.DelegatingDataSource
import org.springframework.security.core.context.SecurityContextHolder
import java.sql.Connection
import javax.sql.DataSource

class MultitenantDataSource(
	private val dataSource: DataSource,
): DelegatingDataSource() {

	companion object {
		const val CURRENT_TENANT_PARAMETER_NAME = "app.tenant"
	}

	init {
		targetDataSource = dataSource
	}

	override fun getConnection(): Connection
			= connectionWithTenant(dataSource.connection)

	override fun getConnection(username: String, password: String): Connection
			= connectionWithTenant(dataSource.getConnection(username, password))

	private fun connectionWithTenant(connection: Connection): Connection {
		val tenantIds = (SecurityContextHolder.getContext().authentication as? JwtAuthentication)
			?.tenantIds
			?: emptyList()
		connection.prepareStatement("SELECT set_config(?, ?, FALSE)").also {
			it.setString(1, CURRENT_TENANT_PARAMETER_NAME)
			it.setString(2, tenantIds.joinToString(prefix = "{", postfix = "}", separator = ","))
		}.execute()
		return connection
	}

}

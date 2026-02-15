package com.github.atzhukov.sbmtdemo.data

import org.springframework.jdbc.datasource.DelegatingDataSource
import java.sql.Connection
import javax.sql.DataSource

class MultitenantDataSource(
    private val dataSource: DataSource,
): DelegatingDataSource() {

    companion object {
        const val CURRENT_TENANT_PARAMETER_NAME = "app.tenant"
    }

    init {
        targetDataSource = dataSource;
    }

    override fun getConnection(): Connection {
        return connectionWithTenant(dataSource.connection)
    }

    override fun getConnection(username: String, password: String): Connection {
        return connectionWithTenant(dataSource.getConnection(username, password))
    }

    private fun connectionWithTenant(connection: Connection): Connection {
        val stmt = connection.prepareStatement("SELECT set_config(?, ?, FALSE)").also {
            it.setString(1, CURRENT_TENANT_PARAMETER_NAME)
            it.setString(2, "{1}") // FIXME
        }
        stmt.execute()
        return connection
    }

}
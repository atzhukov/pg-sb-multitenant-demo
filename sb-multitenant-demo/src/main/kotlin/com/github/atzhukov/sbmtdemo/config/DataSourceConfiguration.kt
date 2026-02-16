package com.github.atzhukov.sbmtdemo.config

import com.github.atzhukov.sbmtdemo.data.MultitenantDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration(
    private val environment: Environment,
) {

    companion object {
        const val PROPERTY_PREFIX_APP = "datasource.app"
        const val PROPERTY_PREFIX_AUTH = "datasource.auth"
    }

    // The primary data source connects as the database user with role 'app',
    // with wide permissions but susceptible to RLS checks.

    @Bean
    @Primary
    fun dataSource(): DataSource {
        val rawDataSource = initDataSource(PROPERTY_PREFIX_APP)
        return MultitenantDataSource(rawDataSource)
    }

    @Bean
    @Primary
    fun jdbcTemplate(@Qualifier("dataSource") dataSource: DataSource): JdbcTemplate
        = JdbcTemplate(dataSource)

    // The secondary auth data source connects as the database user with role 'auth',
    // only allowed to read users and tenants but bypassing RLS to facilitate authentication.

    @Bean
    @Qualifier("authDataSource")
    fun authDataSource(): DataSource = initDataSource(PROPERTY_PREFIX_AUTH)

    @Bean
    @Qualifier("authJdbcTemplate")
    fun authJdbcTemplate(@Qualifier("authDataSource") authDataSource: DataSource): JdbcTemplate
        = JdbcTemplate(authDataSource)

    private fun initDataSource(prefix: String): DataSource {
        val dataSource = DataSourceBuilder.create().build()
        Binder.get(environment).bind(
            prefix,
            Bindable.ofInstance(dataSource).withExistingValue(dataSource)
        )
        return dataSource
    }

}
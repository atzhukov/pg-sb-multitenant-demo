package com.github.atzhukov.sbmtdemo.config

import com.github.atzhukov.sbmtdemo.data.MultitenantDataSource
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration(
    private val environment: Environment,
) {

    companion object {
        const val PROPERTY_PREFIX = "datasource.app"
    }

    @Bean
    @Primary
    fun dataSource(): DataSource {
        val rawDataSource = initDataSource(PROPERTY_PREFIX)
        return MultitenantDataSource(rawDataSource)
    }

    private fun initDataSource(prefix: String): DataSource {
        val dataSource = DataSourceBuilder.create().build()
        Binder.get(environment).bind(
            prefix,
            Bindable.ofInstance(dataSource).withExistingValue(dataSource)
        )
        return dataSource
    }

}
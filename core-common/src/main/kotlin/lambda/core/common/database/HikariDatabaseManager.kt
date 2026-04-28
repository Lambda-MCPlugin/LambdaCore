package lambda.core.common.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import lambda.core.api.database.DatabaseConfig
import lambda.core.api.database.DatabaseManager
import lambda.core.api.database.DatabaseType
import org.bukkit.plugin.Plugin
import java.io.File
import java.sql.Connection

class HikariDatabaseManager(
    private val plugin: Plugin,
    private val config: DatabaseConfig
) : DatabaseManager {

    private var dataSource: HikariDataSource? = null

    init {
        if (config.enabled) {
            connect()
        }
    }

    private fun connect() {
        val hikariConfig = HikariConfig()

        when (config.type) {
            DatabaseType.MYSQL -> {
                hikariConfig.driverClassName = "com.mysql.cj.jdbc.Driver"
                hikariConfig.jdbcUrl =
                    "jdbc:mysql://${config.host}:${config.port}/${config.database}?useSSL=false&characterEncoding=utf8&serverTimezone=UTC"
                hikariConfig.username = config.username
                hikariConfig.password = config.password
            }

            DatabaseType.MARIADB -> {
                hikariConfig.driverClassName = "org.mariadb.jdbc.Driver"
                hikariConfig.jdbcUrl =
                    "jdbc:mariadb://${config.host}:${config.port}/${config.database}?useUnicode=true&characterEncoding=utf8"
                hikariConfig.username = config.username
                hikariConfig.password = config.password
            }

            DatabaseType.SQLITE -> {
                val file = File(plugin.dataFolder, config.file)

                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }

                hikariConfig.driverClassName = "org.sqlite.JDBC"
                hikariConfig.jdbcUrl = "jdbc:sqlite:${file.absolutePath}"
            }
        }

        hikariConfig.maximumPoolSize = config.maximumPoolSize
        hikariConfig.minimumIdle = config.minimumIdle
        hikariConfig.connectionTimeout = config.connectionTimeout
        hikariConfig.idleTimeout = config.idleTimeout
        hikariConfig.maxLifetime = config.maxLifetime
        hikariConfig.poolName = "${plugin.name}-HikariPool"

        dataSource = HikariDataSource(hikariConfig)
    }

    override fun isEnabled(): Boolean {
        return config.enabled
    }

    override fun getConnection(): Connection {
        val source = dataSource
            ?: error("Database가 비활성화되어 있습니다.")

        return source.connection
    }

    override fun <T> useConnection(block: (Connection) -> T): T {
        getConnection().use { connection ->
            return block(connection)
        }
    }

    override fun close() {
        dataSource?.close()
        dataSource = null
    }
}
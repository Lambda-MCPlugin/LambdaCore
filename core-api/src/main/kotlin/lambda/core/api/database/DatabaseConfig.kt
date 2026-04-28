package lambda.core.api.database

data class DatabaseConfig(
    val enabled: Boolean = false,
    val type: DatabaseType = DatabaseType.SQLITE,

    val host: String = "localhost",
    val port: Int = 3306,
    val database: String = "lambdacore",
    val username: String = "root",
    val password: String = "",

    val file: String = "database.db",

    val maximumPoolSize: Int = 10,
    val minimumIdle: Int = 2,
    val connectionTimeout: Long = 30000,
    val idleTimeout: Long = 600000,
    val maxLifetime: Long = 1800000
)
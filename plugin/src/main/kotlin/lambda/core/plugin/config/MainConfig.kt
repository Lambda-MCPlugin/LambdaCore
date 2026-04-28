package lambda.core.plugin.config

import lambda.core.api.config.Config
import lambda.core.api.database.DatabaseConfig

@Config("config.yml")
data class MainConfig(
    val prefix: String = "[LambdaCore]",
    val debug: Boolean = false,
    val database: DatabaseConfig = DatabaseConfig()
)
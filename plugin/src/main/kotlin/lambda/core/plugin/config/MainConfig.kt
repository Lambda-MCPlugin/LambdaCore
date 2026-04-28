package lambda.core.plugin.config

import lambda.core.api.config.Config

@Config("config.yml")
data class MainConfig(
    val prefix: String = "[LambdaCore]",
    val debug: Boolean = false
)
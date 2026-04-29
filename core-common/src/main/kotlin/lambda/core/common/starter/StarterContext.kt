package lambda.core.common.starter

import lambda.core.common.database.AsyncDatabaseTemplate
import lambda.core.common.database.DatabaseTemplate
import lambda.core.common.di.BeanContainer
import org.bukkit.plugin.Plugin

data class StarterContext(
    val plugin: Plugin,
    val container: BeanContainer,
    val databaseTemplate: DatabaseTemplate?,
    val asyncDatabaseTemplate: AsyncDatabaseTemplate?
)
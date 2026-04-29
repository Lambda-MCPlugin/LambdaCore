package lambda.core.plugin

import lambda.core.common.LambdaCoreBootstrap
import lambda.core.common.LambdaCoreProvider
import lambda.core.common.command.argument.ArgumentRegistry
import lambda.core.common.command.argument.BooleanResolver
import lambda.core.common.command.argument.DoubleResolver
import lambda.core.common.command.argument.IntResolver
import lambda.core.common.command.argument.OfflinePlayerResolver
import lambda.core.common.command.argument.PlayerResolver
import lambda.core.common.command.argument.StringResolver
import lambda.core.common.config.LambdaConfig
import lambda.core.common.database.HikariDatabaseManager
import lambda.core.common.nms.NmsProvider
import lambda.core.common.starter.LambdaStarterManager
import lambda.core.common.starter.StarterContext
import lambda.core.platform.folia.FoliaLambdaScheduler
import lambda.core.plugin.config.MainConfig
import lambda.core.plugin.starter.UserStarter
import org.bukkit.plugin.java.JavaPlugin

class LambdaCorePlugin : JavaPlugin() {

    private lateinit var bootstrap: LambdaCoreBootstrap
    private lateinit var starterManager: LambdaStarterManager

    override fun onEnable() {
        val config = LambdaConfig.load(
            plugin = this,
            fileName = "config.yml",
            clazz = MainConfig::class
        )

        val databaseManager = if (config.database.enabled) {
            HikariDatabaseManager(this, config.database)
        } else {
            null
        }

        LambdaCoreProvider.initialize(
            scheduler = FoliaLambdaScheduler(),
            database = databaseManager
        )

        NmsProvider.init()

        registerDefaultArguments()

        bootstrap = LambdaCoreBootstrap(this)
            .scan("lambda.core")
            .start()

        starterManager = LambdaStarterManager(
            StarterContext(
                plugin = this,
                container = bootstrap.container,
                databaseTemplate = LambdaCoreProvider.template,
                asyncDatabaseTemplate = LambdaCoreProvider.asyncTemplate
            )
        )

        starterManager
            .register(UserStarter())
            .startAll { starterId ->
                when (starterId) {
                    "user" -> config.starters.user
                    else -> false
                }
            }

        logger.info("LambdaCore enabled.")
    }

    override fun onDisable() {
        if (::starterManager.isInitialized) {
            starterManager.stopAll()
        }

        LambdaCoreProvider.database?.close()
        LambdaCoreProvider.scheduler?.cancelAll(this)
        LambdaCoreProvider.shutdown()

        logger.info("LambdaCore disabled.")
    }

    fun reloadConfigBeans() {
        bootstrap.container.reloadConfigs()

        val config = bootstrap.container.get(MainConfig::class) as MainConfig

        LambdaCoreProvider.database?.close()

        val databaseManager = if (config.database.enabled) {
            HikariDatabaseManager(this, config.database)
        } else {
            null
        }

        LambdaCoreProvider.setDatabase(databaseManager)
    }

    private fun registerDefaultArguments() {
        ArgumentRegistry.register(PlayerResolver())
        ArgumentRegistry.register(IntResolver())
        ArgumentRegistry.register(StringResolver())
        ArgumentRegistry.register(BooleanResolver())
        ArgumentRegistry.register(DoubleResolver())
        ArgumentRegistry.register(OfflinePlayerResolver())
    }
}
package lambda.core.plugin

import lambda.core.common.LambdaCoreBootstrap
import lambda.core.common.LambdaCoreProvider
import lambda.core.common.command.argument.*
import lambda.core.platform.folia.FoliaLambdaScheduler
import org.bukkit.plugin.java.JavaPlugin

class LambdaCorePlugin : JavaPlugin() {

    private lateinit var bootstrap: LambdaCoreBootstrap

    override fun onEnable() {
        LambdaCoreProvider.initialize(
            scheduler = FoliaLambdaScheduler()
        )

        ArgumentRegistry.register(PlayerResolver())
        ArgumentRegistry.register(IntResolver())
        ArgumentRegistry.register(StringResolver())

        bootstrap = LambdaCoreBootstrap(this)
            .scan("lambda.core")
            .start()
    }

    override fun onDisable() {
        LambdaCoreProvider.scheduler.cancelAll(this)
    }

    fun reloadConfigBeans() {
        bootstrap.container.reloadConfigs()
    }
}
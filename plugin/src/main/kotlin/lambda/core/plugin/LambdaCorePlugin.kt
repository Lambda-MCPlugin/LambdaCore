package lambda.core.plugin

import lambda.core.common.LambdaCoreBootstrap
import lambda.core.common.LambdaCoreProvider
import lambda.core.platform.folia.FoliaLambdaScheduler
import org.bukkit.plugin.java.JavaPlugin

class LambdaCorePlugin : JavaPlugin() {

    override fun onEnable() {
        LambdaCoreProvider.initialize(
            scheduler = FoliaLambdaScheduler()
        )

        LambdaCoreBootstrap(this)
            .scan("lambda.core") // 👈 중요
            .start()

        logger.info("LambdaCore enabled.")
    }

    override fun onDisable() {
        LambdaCoreProvider.scheduler.cancelAll(this)
    }
}
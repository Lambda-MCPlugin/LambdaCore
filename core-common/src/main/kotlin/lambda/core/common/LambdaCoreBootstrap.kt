package lambda.core.common

import lambda.core.api.command.LambdaCommand
import lambda.core.api.config.Config
import lambda.core.api.di.Component
import lambda.core.api.di.Service
import lambda.core.common.command.LambdaCommandManager
import lambda.core.common.di.BeanContainer
import lambda.core.common.di.ClassScanner
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

class LambdaCoreBootstrap(
    private val plugin: Plugin
) {

    val container = BeanContainer(plugin)
    private val commandManager = LambdaCommandManager(plugin)

    fun scan(packageName: String): LambdaCoreBootstrap {
        val classes = ClassScanner.scan(packageName)

        classes.forEach { clazz ->
            if (
                clazz.isAnnotationPresent(Component::class.java) ||
                clazz.isAnnotationPresent(Service::class.java) ||
                clazz.isAnnotationPresent(Config::class.java)
            ) {
                container.register(clazz.kotlin)
            }
        }

        classes.forEach { clazz ->
            if (clazz.isAnnotationPresent(LambdaCommand::class.java)) {
                val instance = container.get(clazz.kotlin)
                commandManager.registerAnnotated(instance)
            }
        }

        classes.forEach { clazz ->
            if (Listener::class.java.isAssignableFrom(clazz)) {
                val instance = container.get(clazz.kotlin) as Listener
                Bukkit.getPluginManager().registerEvents(instance, plugin)
            }
        }

        return this
    }

    fun start(): LambdaCoreBootstrap {
        plugin.logger.info("LambdaCore bootstrap started.")
        return this
    }
}
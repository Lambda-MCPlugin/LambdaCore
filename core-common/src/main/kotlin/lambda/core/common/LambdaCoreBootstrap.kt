package lambda.core.common

import lambda.core.api.command.LambdaCommand
import lambda.core.api.config.Config
import lambda.core.api.database.DatabaseManager
import lambda.core.api.di.Component
import lambda.core.api.di.Repository
import lambda.core.api.di.Service
import lambda.core.api.event.EventListener
import lambda.core.common.command.LambdaCommandManager
import lambda.core.common.database.AsyncDatabaseTemplate
import lambda.core.common.database.DatabaseTemplate
import lambda.core.common.di.BeanContainer
import lambda.core.common.di.ClassScanner
import lambda.core.common.event.LambdaEventRegistrar
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

class LambdaCoreBootstrap(
    private val plugin: Plugin
) {

    val container = BeanContainer(plugin)

    private val commandManager = LambdaCommandManager(plugin)
    private val eventRegistrar = LambdaEventRegistrar(plugin)

    fun scan(packageName: String): LambdaCoreBootstrap {
        registerCoreBeans()

        val classes = ClassScanner.scan(packageName)

        classes.forEach { clazz ->
            if (
                clazz.isAnnotationPresent(Component::class.java) ||
                clazz.isAnnotationPresent(Service::class.java) ||
                clazz.isAnnotationPresent(Repository::class.java) ||
                clazz.isAnnotationPresent(Config::class.java) ||
                hasEventListenerMethod(clazz)
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

        classes.forEach { clazz ->
            if (hasEventListenerMethod(clazz)) {
                val instance = container.get(clazz.kotlin)
                eventRegistrar.register(instance)
            }
        }

        return this
    }

    private fun hasEventListenerMethod(clazz: Class<*>): Boolean {
        return clazz.declaredMethods.any { method ->
            method.isAnnotationPresent(EventListener::class.java)
        }
    }

    private fun registerCoreBeans() {
        LambdaCoreProvider.database?.let { database ->
            container.registerInstance(DatabaseManager::class, database)
        }

        LambdaCoreProvider.template?.let { template ->
            container.registerInstance(DatabaseTemplate::class, template)
        }

        LambdaCoreProvider.asyncTemplate?.let { asyncTemplate ->
            container.registerInstance(AsyncDatabaseTemplate::class, asyncTemplate)
        }
    }

    fun start(): LambdaCoreBootstrap {
        plugin.logger.info("LambdaCore bootstrap started.")
        return this
    }
}
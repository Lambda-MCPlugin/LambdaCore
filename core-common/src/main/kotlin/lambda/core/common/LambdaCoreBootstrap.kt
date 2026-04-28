package lambda.core.common

import lambda.core.api.command.LambdaCommand
import lambda.core.api.di.Component
import lambda.core.api.di.Service
import lambda.core.common.command.LambdaCommandManager
import lambda.core.common.di.BeanContainer
import lambda.core.common.di.ClassScanner
import org.bukkit.plugin.Plugin

class LambdaCoreBootstrap(
    private val plugin: Plugin
) {

    private val container = BeanContainer()
    private val commandManager = LambdaCommandManager(plugin)

    fun scan(packageName: String): LambdaCoreBootstrap {

        val classes = ClassScanner.scan(packageName)

        // 1. Bean 등록
        classes.forEach { clazz ->
            if (
                clazz.isAnnotationPresent(Component::class.java) ||
                clazz.isAnnotationPresent(Service::class.java)
            ) {
                container.register(clazz.kotlin)
            }
        }

        // 2. Command 등록
        classes.forEach { clazz ->
            if (clazz.isAnnotationPresent(LambdaCommand::class.java)) {
                val instance = container.get(clazz.kotlin)
                commandManager.registerAnnotated(instance)
            }
        }

        return this
    }

    fun start() {
        plugin.logger.info("LambdaCore DI started.")
    }
}
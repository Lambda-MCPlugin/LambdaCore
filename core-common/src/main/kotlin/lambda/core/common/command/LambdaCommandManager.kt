package lambda.core.common.command

import lambda.core.api.command.LambdaCommand
import lambda.core.api.command.LambdaCommandExecutor
import lambda.core.api.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.plugin.Plugin

class LambdaCommandManager(
    private val plugin: Plugin
) {

    private val commandMap: CommandMap by lazy {
        val server = Bukkit.getServer()
        val field = server.javaClass.getDeclaredField("commandMap")
        field.isAccessible = true
        field.get(server) as CommandMap
    }

    fun register(
        name: String,
        aliases: List<String> = emptyList(),
        permission: String = "",
        permissionMessage: String = "§c권한이 없습니다.",
        description: String = "",
        executor: LambdaCommandExecutor
    ) {
        val command = LambdaBukkitCommand(
            plugin = plugin,
            name = name,
            description = description,
            aliases = aliases,
            commandPermission = permission,
            permissionMessage = permissionMessage,
            executor = executor
        )

        commandMap.register(plugin.name.lowercase(), command)
    }

    fun registerAnnotated(instance: Any) {
        val commandAnnotation = instance::class.java.getAnnotation(LambdaCommand::class.java)
            ?: error("${instance::class.simpleName} 클래스에 @LambdaCommand가 없습니다.")

        if (instance !is LambdaCommandExecutor) {
            error("${instance::class.simpleName} 클래스는 LambdaCommandExecutor를 구현해야 합니다.")
        }

        val permissionAnnotation = instance::class.java.getAnnotation(Permission::class.java)

        val permission = permissionAnnotation?.value
            ?: commandAnnotation.permission

        val permissionMessage = permissionAnnotation?.message
            ?: "§c권한이 없습니다."

        register(
            name = commandAnnotation.name,
            aliases = commandAnnotation.aliases.toList(),
            permission = permission,
            permissionMessage = permissionMessage,
            description = commandAnnotation.description,
            executor = instance
        )
    }
}
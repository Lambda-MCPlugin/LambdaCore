package lambda.core.common.command

import lambda.core.api.command.LambdaCommand
import lambda.core.api.command.LambdaCommandExecutor
import lambda.core.api.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.Plugin
import java.util.Locale

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

        unregisterOwnPluginYmlCommand(name, aliases)

        val success = commandMap.register(plugin.name.lowercase(), command)

        if (success) {
            plugin.logger.info(
                "Lambda command registered: /$name aliases=$aliases"
            )
            return
        }

        plugin.logger.warning(
            "Lambda command '/$name' conflicted with an existing command and was registered with a fallback prefix. " +
                "Use '/${plugin.name.lowercase(Locale.ROOT)}:$name' or rename the command."
        )
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

    private fun unregisterOwnPluginYmlCommand(
        name: String,
        aliases: List<String>
    ) {
        try {
            val knownCommandsField = findField(commandMap.javaClass, "knownCommands")
                ?: return
            knownCommandsField.isAccessible = true

            @Suppress("UNCHECKED_CAST")
            val knownCommands = knownCommandsField.get(commandMap) as MutableMap<String, Command>

            val targets = buildList {
                add(name.lowercase(Locale.ROOT))
                aliases.forEach { alias -> add(alias.lowercase(Locale.ROOT)) }
                add("${plugin.name.lowercase(Locale.ROOT)}:${name.lowercase(Locale.ROOT)}")
                aliases.forEach { alias ->
                    add("${plugin.name.lowercase(Locale.ROOT)}:${alias.lowercase(Locale.ROOT)}")
                }
            }.toSet()

            for (key in targets) {
                val existing = knownCommands[key] ?: continue
                val owner = (existing as? PluginCommand)?.plugin

                if (owner != plugin) {
                    continue
                }

                existing.unregister(commandMap)
                knownCommands.remove(key)
            }
        } catch (e: Exception) {
            plugin.logger.warning(
                "Failed to clean plugin.yml command duplicates for '/$name': ${e.message}"
            )
        }
    }

    private fun findField(
        type: Class<*>,
        name: String
    ): java.lang.reflect.Field? {
        var current: Class<*>? = type

        while (current != null) {
            try {
                return current.getDeclaredField(name)
            } catch (_: NoSuchFieldException) {
                current = current.superclass
            }
        }

        return null
    }
}

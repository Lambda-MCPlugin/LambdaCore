package lambda.core.common.command

import lambda.core.api.command.LambdaCommandContext
import lambda.core.api.command.LambdaCommandExecutor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.CommandException
import org.bukkit.plugin.Plugin

class LambdaBukkitCommand(
    private val plugin: Plugin,
    name: String,
    description: String,
    aliases: List<String>,
    private val commandPermission: String,
    private val permissionMessage: String,
    private val executor: LambdaCommandExecutor
) : Command(name, description, "/$name", aliases) {

    private val subInvoker = SubCommandInvoker(executor)

    init {
        if (commandPermission.isNotBlank()) {
            setPermission(commandPermission)
            setPermissionMessage(this.permissionMessage)
        }
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String,
        args: Array<out String>
    ): Boolean {
        if (commandPermission.isNotBlank() && !sender.hasPermission(commandPermission)) {
            sender.sendMessage(permissionMessage)
            return true
        }

        val context = LambdaCommandContext(
            plugin = plugin,
            sender = sender,
            label = commandLabel,
            args = Array(args.size) { args[it] }
        )

        try {
            if (subInvoker.invoke(context)) {
                return true
            }

            val result = executor.execute(context)

            if (!result && subInvoker.hasSubCommands()) {
                subInvoker.sendHelp(context)
                return true
            }

            return result
        } catch (e: Exception) {
            plugin.logger.severe("Error while executing command '/$commandLabel': ${e.message}")
            e.printStackTrace()
            throw CommandException("Unhandled exception executing command '/$commandLabel' in ${plugin.name}", e)
        }
    }

    override fun tabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (commandPermission.isNotBlank() && !sender.hasPermission(commandPermission)) {
            return mutableListOf()
        }

        val context = LambdaCommandContext(
            plugin = plugin,
            sender = sender,
            label = alias,
            args = Array(args.size) { args[it] }
        )

        val sub = subInvoker.tabComplete(context)

        if (sub.isNotEmpty()) {
            return sub.toMutableList()
        }

        return executor.tabComplete(context).toMutableList()
    }
}

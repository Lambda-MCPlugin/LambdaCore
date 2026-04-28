package lambda.core.common.command

import lambda.core.api.command.LambdaCommandContext
import lambda.core.api.command.LambdaCommandExecutor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
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

        if (subInvoker.invoke(context)) {
            return true
        }

        return executor.execute(context)
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
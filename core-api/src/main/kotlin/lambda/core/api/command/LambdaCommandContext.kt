package lambda.core.api.command

import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

data class LambdaCommandContext(
    val plugin: Plugin,
    val sender: CommandSender,
    val label: String,
    val args: Array<String>
)
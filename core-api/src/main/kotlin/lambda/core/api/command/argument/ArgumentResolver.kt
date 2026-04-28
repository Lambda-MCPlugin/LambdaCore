package lambda.core.api.command.argument

import org.bukkit.command.CommandSender

interface ArgumentResolver<T : Any> {

    fun supports(type: Class<*>): Boolean

    fun resolve(
        sender: CommandSender,
        input: String
    ): T?
}
package lambda.core.api.command.argument

import org.bukkit.command.CommandSender

interface ArgumentResolver<T : Any> {

    fun supports(type: Class<*>): Boolean

    fun resolve(
        sender: CommandSender,
        input: String
    ): T?

    // TabComplete 추가
    fun suggest(
        sender: CommandSender,
        input: String
    ): List<String> {
        return emptyList()
    }
}
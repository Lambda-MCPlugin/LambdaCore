package lambda.core.common.command.argument

import lambda.core.api.command.argument.ArgumentResolver
import org.bukkit.command.CommandSender

class StringResolver : ArgumentResolver<String> {

    override fun supports(type: Class<*>): Boolean {
        return type == String::class.java
    }

    override fun resolve(sender: CommandSender, input: String): String {
        return input
    }
}
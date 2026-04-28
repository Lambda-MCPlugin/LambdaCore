package lambda.core.common.command.argument

import lambda.core.api.command.argument.ArgumentResolver
import org.bukkit.command.CommandSender

class BooleanResolver : ArgumentResolver<Boolean> {

    override fun supports(type: Class<*>): Boolean {
        return type == Boolean::class.java || type == java.lang.Boolean::class.java
    }

    override fun resolve(sender: CommandSender, input: String): Boolean? {
        return when (input.lowercase()) {
            "true", "on", "yes", "1" -> true
            "false", "off", "no", "0" -> false
            else -> null
        }
    }

    override fun suggest(sender: CommandSender, input: String): List<String> {
        return listOf("true", "false")
            .filter { it.startsWith(input.lowercase()) }
    }
}
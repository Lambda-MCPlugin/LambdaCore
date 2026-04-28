package lambda.core.common.command.argument

import lambda.core.api.command.argument.ArgumentResolver
import org.bukkit.command.CommandSender

class IntResolver : ArgumentResolver<Int> {

    override fun supports(type: Class<*>): Boolean {
        return type == Int::class.java || type == Integer::class.java
    }

    override fun resolve(sender: CommandSender, input: String): Int? {
        return input.toIntOrNull()
    }
}
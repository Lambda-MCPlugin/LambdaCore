package lambda.core.common.command.argument

import lambda.core.api.command.argument.ArgumentResolver
import org.bukkit.command.CommandSender

class DoubleResolver : ArgumentResolver<Double> {

    override fun supports(type: Class<*>): Boolean {
        return type == Double::class.java || type == java.lang.Double::class.java
    }

    override fun resolve(sender: CommandSender, input: String): Double? {
        return input.toDoubleOrNull()
    }
}
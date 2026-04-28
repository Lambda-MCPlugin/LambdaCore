package lambda.core.common.command.argument

import lambda.core.api.command.argument.ArgumentResolver
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayerResolver : ArgumentResolver<Player> {

    override fun supports(type: Class<*>): Boolean {
        return type == Player::class.java
    }

    override fun resolve(sender: CommandSender, input: String): Player? {
        return Bukkit.getPlayerExact(input)
    }
}
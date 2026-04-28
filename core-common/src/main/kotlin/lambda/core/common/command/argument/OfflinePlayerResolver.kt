package lambda.core.common.command.argument

import lambda.core.api.command.argument.ArgumentResolver
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender

class OfflinePlayerResolver : ArgumentResolver<OfflinePlayer> {

    override fun supports(type: Class<*>): Boolean {
        return type == OfflinePlayer::class.java
    }

    override fun resolve(sender: CommandSender, input: String): OfflinePlayer? {
        return Bukkit.getOfflinePlayer(input)
    }
}
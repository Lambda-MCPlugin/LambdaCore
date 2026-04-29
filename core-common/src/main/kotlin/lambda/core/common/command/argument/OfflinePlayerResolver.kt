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

    override fun suggest(sender: CommandSender, input: String): List<String> {
        val lower = input.lowercase()

        val online = Bukkit.getOnlinePlayers()
            .map { it.name }
            .filter { it.lowercase().startsWith(lower) }

        val offline = Bukkit.getOfflinePlayers()
            .mapNotNull { it.name }
            .filter { it.lowercase().startsWith(lower) }

        return (online + offline).distinct()
    }
}

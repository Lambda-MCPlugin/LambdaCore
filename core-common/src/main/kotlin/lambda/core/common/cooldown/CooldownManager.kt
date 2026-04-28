package lambda.core.common.cooldown

import lambda.core.api.cooldown.Cooldown
import lambda.core.api.cooldown.CooldownType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KFunction

object CooldownManager {

    private val cooldowns = ConcurrentHashMap<String, Long>()

    fun check(
        sender: CommandSender,
        commandKey: String,
        function: KFunction<*>,
        classCooldown: Cooldown?
    ): Boolean {
        val functionCooldown = function.annotations
            .filterIsInstance<Cooldown>()
            .firstOrNull()

        val cooldown = functionCooldown ?: classCooldown ?: return true

        val key = createKey(
            sender = sender,
            commandKey = commandKey,
            type = cooldown.type
        )

        val now = System.currentTimeMillis()
        val expireAt = cooldowns[key] ?: 0L

        if (expireAt > now) {
            val remainingSeconds = ((expireAt - now) / 1000L) + 1L

            sender.sendMessage(
                cooldown.message.replace(
                    "%seconds%",
                    remainingSeconds.toString()
                )
            )

            return false
        }

        cooldowns[key] = now + (cooldown.seconds * 1000L)
        return true
    }

    private fun createKey(
        sender: CommandSender,
        commandKey: String,
        type: CooldownType
    ): String {
        return when (type) {
            CooldownType.GLOBAL -> "global:$commandKey"

            CooldownType.PLAYER -> {
                val id = if (sender is Player) {
                    sender.uniqueId
                } else {
                    UUID.nameUUIDFromBytes(sender.name.toByteArray())
                }

                "player:$id:$commandKey"
            }
        }
    }

    fun clear() {
        cooldowns.clear()
    }
}
package lambda.core.api.nms

import org.bukkit.entity.Player

interface NmsAdapter {

    fun sendActionBar(player: Player, message: String)

    fun sendTitle(player: Player, title: String, subtitle: String)
}
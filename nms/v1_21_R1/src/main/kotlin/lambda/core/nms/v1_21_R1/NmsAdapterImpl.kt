package lambda.core.nms.v1_21_R1

import lambda.core.api.nms.NmsAdapter
import org.bukkit.entity.Player

class NmsAdapterImpl : NmsAdapter {

    override fun sendActionBar(player: Player, message: String) {
        player.sendActionBar(message) // 일단 안전하게 Bukkit API
    }

    override fun sendTitle(player: Player, title: String, subtitle: String) {
        player.sendTitle(title, subtitle)
    }
}
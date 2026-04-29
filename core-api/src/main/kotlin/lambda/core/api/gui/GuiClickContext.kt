package lambda.core.api.gui

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

data class GuiClickContext(
    val player: Player,
    val slot: Int,
    val item: ItemStack?,
    val event: InventoryClickEvent
)
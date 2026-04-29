package lambda.core.api.gui

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface LambdaGui {

    val title: String
    val rows: Int

    fun item(
        slot: Int,
        item: ItemStack,
        handler: (GuiClickContext) -> Unit = {}
    ): LambdaGui

    fun open(player: Player)
}
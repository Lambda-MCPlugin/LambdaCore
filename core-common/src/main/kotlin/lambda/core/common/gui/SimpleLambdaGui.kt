package lambda.core.common.gui

import lambda.core.api.gui.GuiClickContext
import lambda.core.api.gui.LambdaGui
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.UUID

class SimpleLambdaGui(
    override val title: String,
    override val rows: Int
) : LambdaGui {

    private val size = rows * 9
    private val items = mutableMapOf<Int, ItemStack>()
    private val handlers = mutableMapOf<Int, (GuiClickContext) -> Unit>()

    override fun item(
        slot: Int,
        item: ItemStack,
        handler: (GuiClickContext) -> Unit
    ): LambdaGui {
        require(slot in 0 until size) {
            "GUI slot must be between 0 and ${size - 1}"
        }

        items[slot] = item
        handlers[slot] = handler
        return this
    }

    override fun open(player: Player) {
        val inventory = Bukkit.createInventory(
            GuiHolder(UUID.randomUUID()),
            size,
            title
        )

        items.forEach { (slot, item) ->
            inventory.setItem(slot, item)
        }

        GuiRegistry.register(inventory, this)

        player.openInventory(inventory)
    }

    fun handleClick(
        player: Player,
        slot: Int,
        clickedItem: ItemStack?,
        event: org.bukkit.event.inventory.InventoryClickEvent
    ) {
        val handler = handlers[slot] ?: return

        handler(
            GuiClickContext(
                player = player,
                slot = slot,
                item = clickedItem,
                event = event
            )
        )
    }
}
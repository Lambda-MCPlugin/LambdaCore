package lambda.core.common.gui

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.UUID

class GuiHolder(
    val id: UUID
) : InventoryHolder {

    override fun getInventory(): Inventory {
        throw UnsupportedOperationException("GuiHolder does not own a static inventory.")
    }
}
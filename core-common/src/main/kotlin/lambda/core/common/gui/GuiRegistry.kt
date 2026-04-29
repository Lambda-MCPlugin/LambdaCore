package lambda.core.common.gui

import org.bukkit.inventory.Inventory
import java.util.WeakHashMap

object GuiRegistry {

    private val guis = WeakHashMap<Inventory, SimpleLambdaGui>()

    fun register(
        inventory: Inventory,
        gui: SimpleLambdaGui
    ) {
        guis[inventory] = gui
    }

    fun find(inventory: Inventory): SimpleLambdaGui? {
        return guis[inventory]
    }

    fun unregister(inventory: Inventory) {
        guis.remove(inventory)
    }
}
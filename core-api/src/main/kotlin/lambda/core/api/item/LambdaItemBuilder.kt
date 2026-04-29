package lambda.core.api.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface LambdaItemBuilder {

    fun material(material: Material): LambdaItemBuilder

    fun amount(amount: Int): LambdaItemBuilder

    fun name(name: String): LambdaItemBuilder

    fun lore(vararg lines: String): LambdaItemBuilder

    fun lore(lines: List<String>): LambdaItemBuilder

    fun customModelData(data: Int): LambdaItemBuilder

    fun glow(enabled: Boolean = true): LambdaItemBuilder

    fun build(): ItemStack
}
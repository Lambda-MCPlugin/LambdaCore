package lambda.core.common.item

import lambda.core.api.item.LambdaItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object LambdaItems {

    fun of(material: Material): LambdaItemBuilder {
        return SimpleItemBuilder(material)
    }

    fun build(
        material: Material,
        block: LambdaItemBuilder.() -> Unit
    ): ItemStack {
        return SimpleItemBuilder(material)
            .apply(block)
            .build()
    }
}
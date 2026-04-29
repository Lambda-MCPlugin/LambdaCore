package lambda.core.common.item

import lambda.core.api.item.LambdaItemBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class SimpleItemBuilder(
    private var material: Material
) : LambdaItemBuilder {

    private var amount: Int = 1
    private var name: String? = null
    private var lore: List<String> = emptyList()
    private var customModelData: Int? = null
    private var glow: Boolean = false

    override fun material(material: Material): LambdaItemBuilder {
        this.material = material
        return this
    }

    override fun amount(amount: Int): LambdaItemBuilder {
        this.amount = amount.coerceIn(1, 64)
        return this
    }

    override fun name(name: String): LambdaItemBuilder {
        this.name = color(name)
        return this
    }

    override fun lore(vararg lines: String): LambdaItemBuilder {
        this.lore = lines.map { color(it) }
        return this
    }

    override fun lore(lines: List<String>): LambdaItemBuilder {
        this.lore = lines.map { color(it) }
        return this
    }

    override fun customModelData(data: Int): LambdaItemBuilder {
        this.customModelData = data
        return this
    }

    override fun glow(enabled: Boolean): LambdaItemBuilder {
        this.glow = enabled
        return this
    }

    override fun build(): ItemStack {
        val item = ItemStack(material, amount)
        val meta = item.itemMeta ?: return item

        if (name != null) {
            meta.setDisplayName(name)
        }

        if (lore.isNotEmpty()) {
            meta.lore = lore
        }

        if (customModelData != null) {
            meta.setCustomModelData(customModelData)
        }

        if (glow) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        item.itemMeta = meta
        return item
    }

    private fun color(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', text)
    }
}
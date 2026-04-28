package lambda.core.common.config

import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

class ReloadableConfig<T : Any>(
    private val plugin: Plugin,
    private val fileName: String,
    private val clazz: KClass<T>
) {

    private var current: T = load()

    fun get(): T = current

    fun reload(): T {
        current = load()
        return current
    }

    private fun load(): T {
        return LambdaConfig.load(plugin, fileName, clazz)
    }
}
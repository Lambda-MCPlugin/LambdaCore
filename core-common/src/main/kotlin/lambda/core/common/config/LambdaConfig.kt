package lambda.core.common.config

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

object LambdaConfig {

    inline fun <reified T : Any> load(
        plugin: Plugin,
        fileName: String
    ): T {
        return load(plugin, fileName, T::class)
    }

    fun <T : Any> load(
        plugin: Plugin,
        fileName: String,
        clazz: KClass<T>
    ): T {
        val file = File(plugin.dataFolder, fileName)

        if (!file.exists()) {
            plugin.dataFolder.mkdirs()
            plugin.saveResource(fileName, false)
        }

        val yaml = YamlConfiguration.loadConfiguration(file)

        return mapToClass(yaml, clazz)
    }

    private fun <T : Any> mapToClass(
        section: ConfigurationSection,
        clazz: KClass<T>
    ): T {
        val constructor = clazz.primaryConstructor
            ?: error("No primary constructor for ${clazz.simpleName}")

        val args = mutableMapOf<KParameter, Any?>()

        for (param in constructor.parameters) {
            val key = param.name ?: continue
            val resolvedKey = resolveKey(section, key)

            if (resolvedKey == null) {
                continue
            }

            val type = param.type.classifier as? KClass<*>
                ?: continue

            val value = readValue(section, resolvedKey, type)

            args[param] = value
        }

        return constructor.callBy(args)
    }

    private fun readValue(
        section: ConfigurationSection,
        key: String,
        type: KClass<*>
    ): Any? {
        return when {
            type == String::class -> section.getString(key)
            type == Int::class -> section.getInt(key)
            type == Long::class -> section.getLong(key)
            type == Double::class -> section.getDouble(key)
            type == Boolean::class -> section.getBoolean(key)

            type.java.isEnum -> {
                val raw = section.getString(key) ?: return null

                type.java.enumConstants
                    .filterIsInstance<Enum<*>>()
                    .firstOrNull { it.name.equals(raw, ignoreCase = true) }
            }

            section.isConfigurationSection(key) -> {
                val child = section.getConfigurationSection(key)
                    ?: return null

                mapToClass(child, type as KClass<Any>)
            }

            else -> section.get(key)
        }
    }

    private fun resolveKey(
        section: ConfigurationSection,
        originalKey: String
    ): String? {
        val candidates = listOf(
            originalKey,
            camelToKebab(originalKey),
            originalKey.replace("_", "-"),
            originalKey.replace("-", "_")
        ).distinct()

        return candidates.firstOrNull { candidate ->
            section.contains(candidate)
        }
    }

    private fun camelToKebab(value: String): String {
        return value.replace(Regex("([a-z0-9])([A-Z])"), "$1-$2")
            .lowercase()
    }
}

package lambda.core.common.config

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import kotlin.reflect.KClass
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
            plugin.saveResource(fileName, false)
        }

        val yaml = YamlConfiguration.loadConfiguration(file)

        return mapToClass(yaml, clazz)
    }

    private fun <T : Any> mapToClass(
        yaml: YamlConfiguration,
        clazz: KClass<T>
    ): T {

        val constructor = clazz.primaryConstructor
            ?: error("No primary constructor for ${clazz.simpleName}")

        val args = constructor.parameters.associateWith { param ->
            val key = param.name ?: return@associateWith null
            yaml.get(key)
        }

        return constructor.callBy(args)
    }
}
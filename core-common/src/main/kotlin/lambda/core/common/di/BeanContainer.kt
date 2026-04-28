package lambda.core.common.di

import lambda.core.api.config.Config
import lambda.core.common.config.LambdaConfig
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class BeanContainer(
    private val plugin: Plugin
) {

    private val beans = mutableMapOf<KClass<*>, Any>()
    private val configBeans = mutableSetOf<KClass<*>>() // config만 추적

    fun register(clazz: KClass<*>) {
        if (beans.containsKey(clazz)) return

        val config = clazz.java.getAnnotation(Config::class.java)

        if (config != null) {
            val instance = LambdaConfig.load(
                plugin = plugin,
                fileName = config.fileName,
                clazz = clazz
            )

            beans[clazz] = instance
            configBeans.add(clazz)
            return
        }

        val constructor = clazz.primaryConstructor
            ?: error("${clazz.simpleName} primary constructor 없음")

        val params = constructor.parameters.map { param ->
            val type = param.type.classifier as KClass<*>
            get(type)
        }

        val instance = constructor.call(*params.toTypedArray())
        beans[clazz] = instance
    }

    fun get(clazz: KClass<*>): Any {
        return beans[clazz] ?: run {
            register(clazz)
            beans[clazz]!!
        }
    }

    inline fun <reified T : Any> get(): T {
        return get(T::class) as T
    }

    // 핵심: config만 reload
    fun reloadConfigs() {
        for (clazz in configBeans) {
            val config = clazz.java.getAnnotation(Config::class.java) ?: continue

            val newInstance = LambdaConfig.load(
                plugin = plugin,
                fileName = config.fileName,
                clazz = clazz
            )

            beans[clazz] = newInstance
        }
    }
}
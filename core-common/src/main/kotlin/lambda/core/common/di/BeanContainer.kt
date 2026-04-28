package lambda.core.common.di

import lambda.core.api.di.Component
import lambda.core.api.di.Service
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class BeanContainer {

    private val beans = mutableMapOf<KClass<*>, Any>()

    fun register(clazz: KClass<*>) {
        if (beans.containsKey(clazz)) return

        val constructor = clazz.primaryConstructor
            ?: error("${clazz.simpleName} 기본 생성자 없음")

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
}
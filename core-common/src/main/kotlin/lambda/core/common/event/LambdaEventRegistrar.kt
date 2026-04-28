package lambda.core.common.event

import lambda.core.api.event.EventListener
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.javaType

class LambdaEventRegistrar(
    private val plugin: Plugin
) {

    fun register(instance: Any) {
        val functions = instance::class.declaredFunctions

        for (function in functions) {
            val annotation = function.annotations
                .filterIsInstance<EventListener>()
                .firstOrNull() ?: continue

            val parameters = function.parameters.drop(1)

            if (parameters.size != 1) {
                plugin.logger.warning("${instance::class.simpleName}.${function.name} 이벤트 함수는 파라미터가 1개여야 합니다.")
                continue
            }

            val eventClass = parameters[0].type.javaType as? Class<*>
                ?: continue

            if (!Event::class.java.isAssignableFrom(eventClass)) {
                plugin.logger.warning("${instance::class.simpleName}.${function.name} 파라미터는 Bukkit Event 타입이어야 합니다.")
                continue
            }

            @Suppress("UNCHECKED_CAST")
            val castedEventClass = eventClass as Class<out Event>

            val listener = object : Listener {}

            val executor = EventExecutor { _, event ->
                if (!castedEventClass.isInstance(event)) {
                    return@EventExecutor
                }

                function.call(instance, event)
            }

            Bukkit.getPluginManager().registerEvent(
                castedEventClass,
                listener,
                annotation.priority,
                executor,
                plugin,
                annotation.ignoreCancelled
            )
        }
    }
}
package lambda.core.common.database

import lambda.core.common.LambdaCoreProvider
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import java.util.concurrent.CompletableFuture

fun <T> CompletableFuture<T>.thenAcceptGlobal(
    plugin: Plugin,
    action: (T) -> Unit
): CompletableFuture<T> {
    return this.whenComplete { result, throwable ->
        if (throwable != null) {
            throwable.printStackTrace()
            return@whenComplete
        }

        LambdaCoreProvider.scheduler?.runGlobal(plugin) {
            action(result)
        }
    }
}

fun <T> CompletableFuture<T>.thenAcceptRegion(
    plugin: Plugin,
    location: Location,
    action: (T) -> Unit
): CompletableFuture<T> {
    return this.whenComplete { result, throwable ->
        if (throwable != null) {
            throwable.printStackTrace()
            return@whenComplete
        }

        LambdaCoreProvider.scheduler?.runRegion(plugin, location) {
            action(result)
        }
    }
}

fun <T> CompletableFuture<T>.thenAcceptEntity(
    plugin: Plugin,
    entity: Entity,
    action: (T) -> Unit
): CompletableFuture<T> {
    return this.whenComplete { result, throwable ->
        if (throwable != null) {
            throwable.printStackTrace()
            return@whenComplete
        }

        LambdaCoreProvider.scheduler?.runEntity(plugin, entity) {
            action(result)
        }
    }
}
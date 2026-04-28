package lambda.core.api.scheduler

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin

interface LambdaScheduler {

    fun runGlobal(
        plugin: Plugin,
        task: () -> Unit
    ): LambdaTask

    fun runGlobalDelayed(
        plugin: Plugin,
        delayTicks: Long,
        task: () -> Unit
    ): LambdaTask

    fun runGlobalTimer(
        plugin: Plugin,
        delayTicks: Long,
        periodTicks: Long,
        task: () -> Unit
    ): LambdaTask

    fun runAsync(
        plugin: Plugin,
        task: () -> Unit
    ): LambdaTask

    fun runAsyncDelayed(
        plugin: Plugin,
        delayTicks: Long,
        task: () -> Unit
    ): LambdaTask

    fun runRegion(
        plugin: Plugin,
        location: Location,
        task: () -> Unit
    ): LambdaTask

    fun runRegionDelayed(
        plugin: Plugin,
        location: Location,
        delayTicks: Long,
        task: () -> Unit
    ): LambdaTask

    fun runEntity(
        plugin: Plugin,
        entity: Entity,
        task: () -> Unit
    ): LambdaTask

    fun runEntityDelayed(
        plugin: Plugin,
        entity: Entity,
        delayTicks: Long,
        task: () -> Unit
    ): LambdaTask

    fun cancelAll(plugin: Plugin)
}
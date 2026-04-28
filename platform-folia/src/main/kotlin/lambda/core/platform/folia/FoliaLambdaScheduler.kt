package lambda.core.platform.folia

import lambda.core.api.scheduler.LambdaScheduler
import lambda.core.api.scheduler.LambdaTask
import lambda.core.platform.folia.task.FoliaLambdaTask
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

class FoliaLambdaScheduler : LambdaScheduler {

    private val tasks = CopyOnWriteArrayList<LambdaTask>()

    private fun track(task: LambdaTask): LambdaTask {
        tasks.add(task)
        return task
    }

    override fun runGlobal(plugin: Plugin, task: () -> Unit): LambdaTask {
        return track(
            FoliaLambdaTask(
                Bukkit.getGlobalRegionScheduler().run(plugin) {
                    task()
                }
            )
        )
    }

    override fun runGlobalDelayed(plugin: Plugin, delayTicks: Long, task: () -> Unit): LambdaTask {
        return track(
            FoliaLambdaTask(
                Bukkit.getGlobalRegionScheduler().runDelayed(plugin, {
                    task()
                }, delayTicks)
            )
        )
    }

    override fun runGlobalTimer(
        plugin: Plugin,
        delayTicks: Long,
        periodTicks: Long,
        task: () -> Unit
    ): LambdaTask {
        return track(
            FoliaLambdaTask(
                Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, {
                    task()
                }, delayTicks, periodTicks)
            )
        )
    }

    override fun runAsync(plugin: Plugin, task: () -> Unit): LambdaTask {
        return track(
            FoliaLambdaTask(
                Bukkit.getAsyncScheduler().runNow(plugin) {
                    task()
                }
            )
        )
    }

    override fun runAsyncDelayed(plugin: Plugin, delayTicks: Long, task: () -> Unit): LambdaTask {
        return track(
            FoliaLambdaTask(
                Bukkit.getAsyncScheduler().runDelayed(
                    plugin,
                    {
                        task()
                    },
                    delayTicks * 50L,
                    TimeUnit.MILLISECONDS
                )
            )
        )
    }

    override fun runRegion(plugin: Plugin, location: Location, task: () -> Unit): LambdaTask {
        return track(
            FoliaLambdaTask(
                Bukkit.getRegionScheduler().run(plugin, location) {
                    task()
                }
            )
        )
    }

    override fun runRegionDelayed(
        plugin: Plugin,
        location: Location,
        delayTicks: Long,
        task: () -> Unit
    ): LambdaTask {
        return track(
            FoliaLambdaTask(
                Bukkit.getRegionScheduler().runDelayed(
                    plugin,
                    location,
                    {
                        task()
                    },
                    delayTicks
                )
            )
        )
    }

    override fun runEntity(plugin: Plugin, entity: Entity, task: () -> Unit): LambdaTask {
        return track(
            FoliaLambdaTask(
                entity.scheduler.run(plugin, {
                    task()
                }, null)
            )
        )
    }

    override fun runEntityDelayed(
        plugin: Plugin,
        entity: Entity,
        delayTicks: Long,
        task: () -> Unit
    ): LambdaTask {
        return track(
            FoliaLambdaTask(
                entity.scheduler.runDelayed(plugin, {
                    task()
                }, null, delayTicks)
            )
        )
    }

    override fun cancelAll(plugin: Plugin) {
        Bukkit.getGlobalRegionScheduler().cancelTasks(plugin)
        Bukkit.getAsyncScheduler().cancelTasks(plugin)

        tasks.forEach { it.cancel() }
        tasks.clear()
    }
}
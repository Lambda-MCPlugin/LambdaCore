package lambda.core.platform.folia.task

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import lambda.core.api.scheduler.LambdaTask

class FoliaLambdaTask(
    private val task: ScheduledTask?
) : LambdaTask {

    override fun cancel() {
        task?.cancel()
    }
}
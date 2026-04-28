package lambda.core.common

import lambda.core.api.scheduler.LambdaScheduler

object LambdaCoreProvider {

    lateinit var scheduler: LambdaScheduler
        private set

    fun initialize(scheduler: LambdaScheduler) {
        this.scheduler = scheduler
    }
}
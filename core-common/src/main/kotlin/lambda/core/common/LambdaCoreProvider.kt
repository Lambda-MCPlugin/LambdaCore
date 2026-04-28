package lambda.core.common

import lambda.core.api.database.DatabaseManager
import lambda.core.api.scheduler.LambdaScheduler
import lambda.core.common.database.AsyncDatabaseTemplate
import lambda.core.common.database.DatabaseTemplate

object LambdaCoreProvider {

    var scheduler: LambdaScheduler? = null
        private set

    var database: DatabaseManager? = null
        private set

    var template: DatabaseTemplate? = null
        private set

    var asyncTemplate: AsyncDatabaseTemplate? = null
        private set

    fun initialize(
        scheduler: LambdaScheduler,
        database: DatabaseManager? = null
    ) {
        this.scheduler = scheduler
        this.database = database
        this.template = database?.let { DatabaseTemplate(it) }
        this.asyncTemplate = this.template?.let { AsyncDatabaseTemplate(it) }
    }

    fun setDatabase(database: DatabaseManager?) {
        this.database = database
        this.template = database?.let { DatabaseTemplate(it) }
        this.asyncTemplate = this.template?.let { AsyncDatabaseTemplate(it) }
    }

    fun shutdown() {
        database?.close()

        database = null
        template = null
        asyncTemplate = null
        scheduler = null
    }
}
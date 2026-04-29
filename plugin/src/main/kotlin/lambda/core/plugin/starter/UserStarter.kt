package lambda.core.plugin.starter

import lambda.core.common.starter.LambdaStarter
import lambda.core.common.starter.StarterContext

class UserStarter : LambdaStarter {

    override val id: String = "user"
    override val name: String = "User Starter"

    override fun start(context: StarterContext) {
        val db = context.databaseTemplate

        if (db == null) {
            context.plugin.logger.warning("User Starter requires database enabled.")
            return
        }

        db.update(
            """
            CREATE TABLE IF NOT EXISTS lambdacore_users (
                uuid VARCHAR(36) PRIMARY KEY,
                name VARCHAR(16) NOT NULL,
                first_join BIGINT NOT NULL,
                last_join BIGINT NOT NULL
            )
            """.trimIndent()
        )
    }
}
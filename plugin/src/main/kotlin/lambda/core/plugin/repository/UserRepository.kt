package lambda.core.plugin.repository

import lambda.core.api.di.Repository
import lambda.core.common.database.DatabaseTemplate

@Repository
class UserRepository(
    private val db: DatabaseTemplate
) {

    fun createTable() {
        db.update(
            """
            CREATE TABLE IF NOT EXISTS users (
                uuid VARCHAR(36) PRIMARY KEY,
                name VARCHAR(16) NOT NULL
            )
            """.trimIndent()
        )
    }

    fun save(uuid: String, name: String) {
        db.update(
            """
            INSERT INTO users (uuid, name)
            VALUES (?, ?)
            """.trimIndent(),
            uuid,
            name
        )
    }

    fun findName(uuid: String): String? {
        return db.queryOne(
            "SELECT name FROM users WHERE uuid = ?",
            uuid
        ) { rs ->
            rs.getString("name")
        }
    }
}
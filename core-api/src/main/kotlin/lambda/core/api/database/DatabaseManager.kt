package lambda.core.api.database

import java.sql.Connection

interface DatabaseManager {

    fun isEnabled(): Boolean

    fun getConnection(): Connection

    fun <T> useConnection(block: (Connection) -> T): T

    fun close()
}
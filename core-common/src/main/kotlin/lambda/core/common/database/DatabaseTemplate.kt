package lambda.core.common.database

import lambda.core.api.database.DatabaseManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class DatabaseTemplate(
    private val database: DatabaseManager
) {

    fun update(
        sql: String,
        vararg params: Any?
    ): Int {
        return database.useConnection { connection ->
            connection.prepareStatement(sql).use { stmt ->
                setParams(stmt, params)
                stmt.executeUpdate()
            }
        }
    }

    fun <T> query(
        sql: String,
        vararg params: Any?,
        mapper: (ResultSet) -> T
    ): List<T> {
        return database.useConnection { connection ->
            connection.prepareStatement(sql).use { stmt ->
                setParams(stmt, params)

                stmt.executeQuery().use { rs ->
                    val list = mutableListOf<T>()

                    while (rs.next()) {
                        list.add(mapper(rs))
                    }

                    list
                }
            }
        }
    }

    fun <T> queryOne(
        sql: String,
        vararg params: Any?,
        mapper: (ResultSet) -> T
    ): T? {
        return database.useConnection { connection ->
            connection.prepareStatement(sql).use { stmt ->
                setParams(stmt, params)

                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        mapper(rs)
                    } else {
                        null
                    }
                }
            }
        }
    }

    fun transaction(block: (java.sql.Connection) -> Unit) {
        database.useConnection { connection ->
            try {
                connection.autoCommit = false

                block(connection)

                connection.commit()
            } catch (e: Exception) {
                connection.rollback()
                throw e
            } finally {
                connection.autoCommit = true
            }
        }
    }

    private fun setParams(stmt: PreparedStatement, params: Array<out Any?>) {
        for ((index, param) in params.withIndex()) {
            stmt.setObject(index + 1, param)
        }
    }
}
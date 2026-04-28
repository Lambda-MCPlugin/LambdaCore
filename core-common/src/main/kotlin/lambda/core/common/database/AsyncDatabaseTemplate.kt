package lambda.core.common.database

import java.sql.Connection
import java.sql.ResultSet
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AsyncDatabaseTemplate(
    private val template: DatabaseTemplate,
    private val executor: Executor = Executors.newCachedThreadPool()
) {

    fun updateAsync(
        sql: String,
        vararg params: Any?
    ): CompletableFuture<Int> {
        return CompletableFuture.supplyAsync({
            template.update(sql, *params)
        }, executor)
    }

    fun <T> queryAsync(
        sql: String,
        vararg params: Any?,
        mapper: (ResultSet) -> T
    ): CompletableFuture<List<T>> {
        return CompletableFuture.supplyAsync({
            template.query(sql, *params, mapper = mapper)
        }, executor)
    }

    fun <T> queryOneAsync(
        sql: String,
        vararg params: Any?,
        mapper: (ResultSet) -> T
    ): CompletableFuture<T?> {
        return CompletableFuture.supplyAsync({
            template.queryOne(sql, *params, mapper = mapper)
        }, executor)
    }

    fun transactionAsync(
        block: (Connection) -> Unit
    ): CompletableFuture<Void> {
        return CompletableFuture.runAsync({
            template.transaction(block)
        }, executor)
    }
}
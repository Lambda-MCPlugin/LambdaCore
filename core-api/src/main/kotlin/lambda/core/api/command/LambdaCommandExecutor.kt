package lambda.core.api.command

interface LambdaCommandExecutor {

    fun execute(context: LambdaCommandContext): Boolean

    fun tabComplete(context: LambdaCommandContext): List<String> {
        return emptyList()
    }
}
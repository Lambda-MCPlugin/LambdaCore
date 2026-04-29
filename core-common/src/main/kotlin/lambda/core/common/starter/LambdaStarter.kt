package lambda.core.common.starter

interface LambdaStarter {

    val id: String
    val name: String

    fun start(context: StarterContext)

    fun stop(context: StarterContext) {
    }
}
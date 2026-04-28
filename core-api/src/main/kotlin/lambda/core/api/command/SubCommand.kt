package lambda.core.api.command

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SubCommand(
    val name: String,
    val aliases: Array<String> = []
)
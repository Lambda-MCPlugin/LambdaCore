package lambda.core.api.command

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LambdaCommand(
    val name: String,
    val aliases: Array<String> = [],
    val permission: String = "",
    val description: String = ""
)
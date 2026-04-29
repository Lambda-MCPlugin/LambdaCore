package lambda.core.api.command

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Usage(
    val value: String
)
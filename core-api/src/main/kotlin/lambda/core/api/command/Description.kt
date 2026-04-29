package lambda.core.api.command

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Description(
    val value: String
)
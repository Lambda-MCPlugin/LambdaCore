package lambda.core.api.permission

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Permission(
    val value: String,
    val message: String = "§c권한이 없습니다."
)
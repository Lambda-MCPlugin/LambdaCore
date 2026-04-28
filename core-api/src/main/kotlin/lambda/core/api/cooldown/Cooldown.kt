package lambda.core.api.cooldown

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Cooldown(
    val seconds: Long,
    val type: CooldownType = CooldownType.PLAYER,
    val message: String = "§c잠시 후 다시 시도해주세요. 남은 시간: %seconds%초"
)
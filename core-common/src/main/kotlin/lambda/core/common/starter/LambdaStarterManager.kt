package lambda.core.common.starter

class LambdaStarterManager(
    private val context: StarterContext
) {

    private val starters = mutableListOf<LambdaStarter>()
    private val started = mutableListOf<LambdaStarter>()

    fun register(starter: LambdaStarter): LambdaStarterManager {
        starters.add(starter)
        return this
    }

    fun startAll(isEnabled: (String) -> Boolean) {
        for (starter in starters) {
            if (!isEnabled(starter.id)) {
                context.plugin.logger.info("Starter skipped: ${starter.name}")
                continue
            }

            starter.start(context)
            started.add(starter)

            context.plugin.logger.info("Starter enabled: ${starter.name}")
        }
    }

    fun stopAll() {
        for (starter in started.asReversed()) {
            starter.stop(context)
            context.plugin.logger.info("Starter disabled: ${starter.name}")
        }

        started.clear()
    }
}
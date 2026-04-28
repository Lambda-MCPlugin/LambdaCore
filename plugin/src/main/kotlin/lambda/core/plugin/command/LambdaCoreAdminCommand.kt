package lambda.core.plugin.command

import lambda.core.api.command.*
import lambda.core.common.LambdaCoreProvider

@LambdaCommand(
    name = "lambdacore",
    aliases = ["lc"],
    permission = "lambdacore.admin"
)
class LambdaCoreAdminCommand : LambdaCommandExecutor {

    override fun execute(context: LambdaCommandContext): Boolean {
        context.sender.sendMessage("§b/lambdacore reload | scheduler")
        return true
    }

    @SubCommand("reload")
    fun reload(context: LambdaCommandContext) {
        val plugin = context.plugin as LambdaCorePlugin

        plugin.reloadConfigBeans()

        context.sender.sendMessage("§aConfig reload 완료")
    }

    @SubCommand("scheduler")
    fun scheduler(context: LambdaCommandContext) {
        LambdaCoreProvider.scheduler.runGlobal(context.plugin) {
            context.sender.sendMessage("§aScheduler 테스트 완료")
        }
    }
}
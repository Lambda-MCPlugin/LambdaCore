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
        context.sender.sendMessage("§a리로드 완료")
    }

    @SubCommand("scheduler")
    fun scheduler(context: LambdaCommandContext) {
        LambdaCoreProvider.scheduler.runGlobal(context.plugin) {
            context.sender.sendMessage("§aScheduler 테스트 완료")
        }
    }
}
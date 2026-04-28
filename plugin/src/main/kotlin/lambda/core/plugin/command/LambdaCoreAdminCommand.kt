package lambda.core.plugin.command

import lambda.core.api.command.LambdaCommand
import lambda.core.api.command.LambdaCommandContext
import lambda.core.api.command.LambdaCommandExecutor
import lambda.core.common.LambdaCoreProvider

@LambdaCommand(
    name = "lambdacore",
    aliases = ["lc"],
    permission = "lambdacore.admin",
    description = "LambdaCore admin command"
)
class LambdaCoreAdminCommand : LambdaCommandExecutor {

    override fun execute(context: LambdaCommandContext): Boolean {
        val sender = context.sender

        if (context.args.isEmpty()) {
            sender.sendMessage("§bLambdaCore §f명령어")
            sender.sendMessage("§7/lambdacore reload")
            return true
        }

        when (context.args[0].lowercase()) {
            "reload" -> {
                sender.sendMessage("§aLambdaCore reload 완료.")
                return true
            }

            "scheduler" -> {
                LambdaCoreProvider.scheduler.runGlobal(context.plugin) {
                    sender.sendMessage("§aGlobal Scheduler 테스트 완료.")
                }
                return true
            }

            else -> {
                sender.sendMessage("§c알 수 없는 명령어입니다.")
                return true
            }
        }
    }

    override fun tabComplete(context: LambdaCommandContext): List<String> {
        if (context.args.size == 1) {
            return listOf("reload", "scheduler")
                .filter { it.startsWith(context.args[0].lowercase()) }
        }

        return emptyList()
    }
}
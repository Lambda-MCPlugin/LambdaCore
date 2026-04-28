package lambda.core.common.command

import lambda.core.api.command.LambdaCommandContext
import lambda.core.api.command.SubCommand
import lambda.core.common.command.argument.ArgumentRegistry
import org.bukkit.command.CommandSender
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.javaType

class SubCommandInvoker(
    private val instance: Any
) {

    private val subCommands: Map<String, KFunction<*>> = buildMap {
        val functions = instance::class.declaredFunctions

        for (function in functions) {
            val annotation = function.annotations
                .filterIsInstance<SubCommand>()
                .firstOrNull() ?: continue

            put(annotation.name.lowercase(), function)

            for (alias in annotation.aliases) {
                put(alias.lowercase(), function)
            }
        }
    }

    fun invoke(context: LambdaCommandContext): Boolean {
        if (context.args.isEmpty()) return false

        val sub = context.args[0].lowercase()
        val function = subCommands[sub] ?: return false

        val args = buildArguments(function, context) ?: return true

        function.call(instance, *args.toTypedArray())
        return true
    }

    private fun buildArguments(
        function: KFunction<*>,
        context: LambdaCommandContext
    ): List<Any?>? {

        val result = mutableListOf<Any?>()
        val params = function.parameters.drop(1) // instance 제외

        var argIndex = 1 // args[0] = subcommand

        for (param in params) {
            val type = param.type.javaType as Class<*>

            if (type == LambdaCommandContext::class.java) {
                result.add(context)
                continue
            }

            val input = context.args.getOrNull(argIndex)
                ?: return null

            val resolver = ArgumentRegistry.find(type)
                ?: error("Resolver 없음: $type")

            val value = resolver.resolve(context.sender, input)
                ?: run {
                    context.sender.sendMessage("§c잘못된 값: $input")
                    return null
                }

            result.add(value)
            argIndex++
        }

        return result
    }

    fun tabComplete(context: LambdaCommandContext): List<String> {
        if (context.args.size == 1) {
            val input = context.args[0].lowercase()

            return subCommands.keys
                .distinct()
                .filter { it.startsWith(input) }
        }

        return emptyList()
    }
}
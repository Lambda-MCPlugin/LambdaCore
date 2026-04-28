package lambda.core.common.command

import lambda.core.api.async.Async
import lambda.core.api.command.LambdaCommandContext
import lambda.core.api.command.SubCommand
import lambda.core.api.permission.Permission
import lambda.core.common.LambdaCoreProvider
import lambda.core.common.command.argument.ArgumentRegistry
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.javaType

class SubCommandInvoker(
    private val instance: Any
) {

    private data class RegisteredSubCommand(
        val path: List<String>,
        val function: KFunction<*>
    )

    private val subCommands: List<RegisteredSubCommand> = buildList {
        val functions = instance::class.declaredFunctions

        for (function in functions) {
            val annotation = function.annotations
                .filterIsInstance<SubCommand>()
                .firstOrNull() ?: continue

            add(
                RegisteredSubCommand(
                    path = annotation.name.lowercase().split(" "),
                    function = function
                )
            )

            for (alias in annotation.aliases) {
                add(
                    RegisteredSubCommand(
                        path = alias.lowercase().split(" "),
                        function = function
                    )
                )
            }
        }
    }

    fun invoke(context: LambdaCommandContext): Boolean {
        if (context.args.isEmpty()) {
            return false
        }

        val matched = findMatchedSubCommand(context.args) ?: return false
        val function = matched.function

        val permission = function.annotations
            .filterIsInstance<Permission>()
            .firstOrNull()

        if (permission != null && !context.sender.hasPermission(permission.value)) {
            context.sender.sendMessage(permission.message)
            return true
        }

        val args = buildArguments(
            function = function,
            context = context,
            subCommandDepth = matched.path.size
        ) ?: return true

        val isAsync = function.annotations.any { it is Async }

        if (isAsync) {
            LambdaCoreProvider.scheduler?.runAsync(context.plugin) {
                try {
                    function.call(instance, *args.toTypedArray())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } ?: context.sender.sendMessage("§cScheduler가 초기화되지 않았습니다.")
        } else {
            function.call(instance, *args.toTypedArray())
        }

        return true
    }

    private fun findMatchedSubCommand(args: Array<String>): RegisteredSubCommand? {
        return subCommands
            .sortedByDescending { it.path.size }
            .firstOrNull { subCommand ->
                if (args.size < subCommand.path.size) {
                    return@firstOrNull false
                }

                subCommand.path.withIndex().all { (index, value) ->
                    args[index].equals(value, ignoreCase = true)
                }
            }
    }

    private fun buildArguments(
        function: KFunction<*>,
        context: LambdaCommandContext,
        subCommandDepth: Int
    ): List<Any?>? {
        val result = mutableListOf<Any?>()
        val params = function.parameters.drop(1)

        var argIndex = subCommandDepth

        for (param in params) {
            val type = param.type.javaType as Class<*>

            if (type == LambdaCommandContext::class.java) {
                result.add(context)
                continue
            }

            val input = context.args.getOrNull(argIndex)
                ?: run {
                    context.sender.sendMessage("§c인자가 부족합니다.")
                    return null
                }

            val resolver = ArgumentRegistry.find(type)

            val value = when {
                resolver != null -> resolver.resolve(context.sender, input)

                type.isEnum -> {
                    type.enumConstants
                        ?.map { it as Enum<*> }
                        ?.firstOrNull { it.name.equals(input, ignoreCase = true) }
                }

                else -> error("Resolver 없음: $type")
            }

            if (value == null) {
                context.sender.sendMessage("§c잘못된 값: $input")
                return null
            }

            result.add(value)
            argIndex++
        }

        return result
    }

    fun tabComplete(context: LambdaCommandContext): List<String> {
        if (context.args.isEmpty()) {
            return emptyList()
        }

        val currentIndex = context.args.size - 1
        val input = context.args[currentIndex].lowercase()

        val pathSuggestions = subCommands
            .mapNotNull { subCommand ->
                if (currentIndex >= subCommand.path.size) {
                    return@mapNotNull null
                }

                val previousMatches = subCommand.path
                    .take(currentIndex)
                    .withIndex()
                    .all { (index, value) ->
                        context.args[index].equals(value, ignoreCase = true)
                    }

                if (!previousMatches) {
                    return@mapNotNull null
                }

                val next = subCommand.path[currentIndex]

                if (next.startsWith(input)) {
                    next
                } else {
                    null
                }
            }
            .distinct()

        if (pathSuggestions.isNotEmpty()) {
            return pathSuggestions
        }

        val matched = findMatchedSubCommand(context.args) ?: return emptyList()
        val function = matched.function
        val params = function.parameters.drop(1)

        var argIndex = matched.path.size

        for (param in params) {
            val type = param.type.javaType as Class<*>

            if (type == LambdaCommandContext::class.java) {
                continue
            }

            if (argIndex == currentIndex) {
                val resolver = ArgumentRegistry.find(type)

                if (resolver != null) {
                    return resolver.suggest(context.sender, context.args[argIndex])
                }

                if (type.isEnum) {
                    return type.enumConstants
                        ?.map { (it as Enum<*>).name.lowercase() }
                        ?.filter { it.startsWith(context.args[argIndex].lowercase()) }
                        ?: emptyList()
                }

                return emptyList()
            }

            argIndex++
        }

        return emptyList()
    }
}
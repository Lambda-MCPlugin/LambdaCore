package lambda.core.common.command.argument

import lambda.core.api.command.argument.ArgumentResolver

object ArgumentRegistry {

    private val resolvers = mutableListOf<ArgumentResolver<*>>()

    fun register(resolver: ArgumentResolver<*>) {
        resolvers.add(resolver)
    }

    fun find(type: Class<*>): ArgumentResolver<*>? {
        return resolvers.firstOrNull { it.supports(type) }
    }
}
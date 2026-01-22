package statement

import evaluator.LiteralValue

class VariableNotFoundException(message: String): RuntimeException(message)

class Environment {
    private val map: MutableMap<String, VariableValue> = mutableMapOf()

    fun get(name: String): VariableValue {
        return map[name] ?: throw VariableNotFoundException("Undefined variable '$name'.")
    }

    fun set(name: String, value: LiteralValue) {
        map[name] = VariableValue(name, value)
    }

    fun contains(name: String): Boolean = map.containsKey(name)

    fun remove(name: String) = map.remove(name)

    fun isEmpty() = map.isEmpty()

    fun size() = map.size
}

object GlobalEnvironment {
    private val scopeStack = mutableListOf(Environment())

    fun get(name: String): VariableValue {
        return scopeStack.reversed().firstOrNull { it.contains(name) }
            ?.get(name)
            ?: throw VariableNotFoundException("Undefined variable '$name'.")
    }

    fun define(name: String, value: LiteralValue) {
        scopeStack.last().set(name, value)
    }

    fun assign(name: String, value: LiteralValue): Boolean {
        for (i in scopeStack.size - 1 downTo 0) {
            if (scopeStack[i].contains(name)) {
                scopeStack[i].set(name, value)
                return true
            }
        }
        throw VariableNotFoundException("Undefined variable '$name'.")
    }

    fun set(name: String, value: LiteralValue) {
        scopeStack.last().set(name, value)
    }

    fun pushScope() {
        scopeStack.add(Environment())
    }

    fun popScope() {
        if (scopeStack.size > 1) {
            scopeStack.removeLast()
        }
    }

    fun currentScope(): Environment = scopeStack.last()

    fun clear() {
        scopeStack.clear()
        scopeStack.add(Environment())
    }

    fun size() = scopeStack.last().size()

    fun contains(key: String) = scopeStack.last().contains(key)
}
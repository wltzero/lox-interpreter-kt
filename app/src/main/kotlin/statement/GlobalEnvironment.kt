package statement

import evaluator.LiteralValue

class VariableNotFoundException(message: String): RuntimeException(message)

object GlobalEnvironment {
    private val map: MutableMap<String, VariableValue> = mutableMapOf()
    fun get(name: String): VariableValue{
        return map[name] ?: throw VariableNotFoundException("Undefined variable '$name'.")
    }
    fun set(name: String, value: LiteralValue) {
        map[name] = VariableValue(name, value)
    }

    fun remove(name: String) = map.remove(name)
    fun clear() = map.clear()
    fun isEmpty() = map.isEmpty()
    fun size() = map.size
    fun contains(key: String) = map.contains(key)

}
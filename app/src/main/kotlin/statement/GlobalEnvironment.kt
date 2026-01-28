package statement

import evaluator.LiteralValue
import exception.VariableNotFoundException
import parser.ASTNode

data class Environment(val parent: Environment? = null) {
    val map: MutableMap<String, VariableValue> = mutableMapOf()

    fun get(name: String): VariableValue {
        return map[name] ?: throw VariableNotFoundException("Undefined variable '$name'.")
    }

    fun set(name: String, value: LiteralValue) {
        map[name] = VariableValue(name, value)
    }

    fun contains(name: String): Boolean = map.containsKey(name)
}

object GlobalEnvironment {
    private val scopeStack = mutableListOf(Environment())

    init{
        registryNativeFunction("clock", emptyList(), LiteralValue.NilLiteralValue) {
            LiteralValue.IntegerLiteralValue((System.currentTimeMillis() / 1000).toInt())
        }
    }

    fun registryNativeFunction(name:String, parameters:List<String>, res: LiteralValue, function: (List<LiteralValue>)-> LiteralValue){
        define(name, LiteralValue.NativeFunctionLiteralValue(name, parameters, res, function))
    }

    fun registryFunction(name: String, parameters: List<String>, body: List<ASTNode.Stmt>) {
        val capturedEnv = getCurrentEnvironment()
        define(name, LiteralValue.FunctionLiteralValue(name, parameters, body, capturedEnv))
    }

    fun getCurrentEnvironment(): Environment {
        return scopeStack.last()
    }

    private fun ancestor(distance: Int): Environment {
        var env = getCurrentEnvironment()
        repeat(distance) {
            env = env.parent ?: env
        }
        return env
    }

    fun getAt(distance: Int, name: String): VariableValue {
        val env = ancestor(distance)
        var current: Environment? = env
        while (current != null) {
            if (current.contains(name)) {
                return current.get(name)
            }
            current = current.parent
        }
        throw VariableNotFoundException("Undefined variable '$name'.")
    }

    fun assignAt(distance: Int, name: String, value: LiteralValue) {
        ancestor(distance).set(name, value)
    }

    fun getGlobal(name: String): VariableValue {
        return scopeStack.first().get(name)
    }

    fun assignGlobal(name: String, value: LiteralValue) {
        scopeStack.first().set(name, value)
    }

    fun get(name: String): VariableValue {
        var env: Environment = getCurrentEnvironment()
        while(true){
            if (env.contains(name)) {
                return env.get(name)
            }
            if(env.parent==null){
                break
            }
            env = env.parent
        }
        for (i in scopeStack.size - 1 downTo 0) {
            if (scopeStack[i].contains(name)) {
                return scopeStack[i].get(name)
            }
        }
        throw VariableNotFoundException("Undefined variable '$name'.")
    }

    fun define(name: String, value: LiteralValue) {
        scopeStack.last().set(name, value)
    }

    fun assign(name: String, value: LiteralValue): Boolean {
        var env: Environment = getCurrentEnvironment()
        while(true){
            if (env.contains(name)) {
                env.set(name, value)
                return true
            }
            if(env.parent==null){
                break
            }
            env = env.parent
        }
        for (i in scopeStack.size - 1 downTo 0) {
            if (scopeStack[i].contains(name)) {
                scopeStack[i].set(name, value)
                return true
            }
        }
        throw VariableNotFoundException("Undefined variable '$name'.")
    }

    fun pushScope() {
        val newEnv = Environment(scopeStack.last())
        scopeStack.add(newEnv)
    }

    fun pushScopeWith(env: Environment){
        val newEnv = Environment(env)
        scopeStack.add(newEnv)
    }

    fun popScope() {
        if (scopeStack.size > 1) {
            scopeStack.removeLast()
        }
    }

    fun reset() {
        scopeStack.clear()
        scopeStack.add(Environment())
        registryNativeFunction("clock", emptyList(), LiteralValue.NilLiteralValue) {
            LiteralValue.IntegerLiteralValue((System.currentTimeMillis() / 1000).toInt())
        }
    }

}

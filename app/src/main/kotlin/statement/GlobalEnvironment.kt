package statement

import evaluator.LiteralValue
import exception.VariableNotFoundException
import parser.ASTNode

class Environment: Cloneable {
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
    private var blockDepth = 0
    private var closureEnv: Environment? = null

    init{
        registryNativeFunction("clock", emptyList(), LiteralValue.NilLiteralValue) {
            LiteralValue.IntegerLiteralValue((System.currentTimeMillis() / 1000).toInt())
        }
    }

    fun registryNativeFunction(name:String, parameters:List<String>, res: LiteralValue, function: (List<LiteralValue>)-> LiteralValue){
        define(name, LiteralValue.NativeFunctionLiteralValue(name, parameters, res, function))
    }

    fun registryFunction(name: String, parameters: List<String>, body: List<ASTNode.Stmt>) {
        val capturedEnv = captureCurrentEnvironment()
        define(name, LiteralValue.FunctionLiteralValue(name, parameters, body, capturedEnv, blockDepth))
    }

    private fun captureCurrentEnvironment(): Environment {
        val env = Environment()
        for (i in scopeStack.size - 1 downTo 0) {
            scopeStack[i].map.forEach { (key, value) ->
                if (!env.contains(key)) {
                    env.set(key, value.value)
                }
            }
        }
        return env
    }

    fun get(name: String): VariableValue {
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
        for (i in scopeStack.size - 1 downTo 0) {
            if (scopeStack[i].contains(name)) {
                scopeStack[i].set(name, value)
                return true
            }
        }
        throw VariableNotFoundException("Undefined variable '$name'.")
    }

    fun pushScope() {
        scopeStack.add(Environment())
    }

    fun popScope() {
        if (scopeStack.size > 1) {
            scopeStack.removeLast()
        }
    }

    fun enterBlock() {
        blockDepth++
    }

    fun exitBlock() {
        if (blockDepth > 0) {
            blockDepth--
        }
    }

    fun getBlockDepth(): Int = blockDepth

    fun setClosureEnv(env: Environment?) {
        closureEnv = env
    }

    fun getClosureEnv(): Environment? = closureEnv

    fun getWithClosure(name: String): VariableValue {
        if (closureEnv != null && closureEnv!!.contains(name)) {
            return closureEnv!!.get(name)
        }
        return get(name)
    }

    fun assignWithClosure(name: String, value: LiteralValue): Boolean {
        if (closureEnv != null && closureEnv!!.contains(name)) {
            closureEnv!!.set(name, value)
            return true
        }
        return assign(name, value)
    }
}
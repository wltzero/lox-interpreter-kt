package evaluator

import parser.ASTNode
import statement.Environment

sealed class LiteralValue {
    class DoubleLiteralValue(val value: Double) : LiteralValue()
    class IntegerLiteralValue(val value: Int) : LiteralValue()
    class StringLiteralValue(val value: String) : LiteralValue()
    class BooleanLiteralValue(val value: Boolean) : LiteralValue()
    object NilLiteralValue : LiteralValue()
    class IdentifierLiteralValue(val name: String) : LiteralValue()
    class NativeFunctionLiteralValue(val name:String, val parameters:List<String>, val res: LiteralValue,val function: (List<LiteralValue>)-> LiteralValue) : LiteralValue()
    class FunctionLiteralValue(
        val name: String,
        val parameters: List<String>,
        val body: List<ASTNode.Stmt>,
        val capturedEnvironment: Environment? = null
    ) : LiteralValue()


    fun asDouble(): Double = when (this) {
        is DoubleLiteralValue -> value
        is IntegerLiteralValue -> value.toDouble()
        else -> throw RuntimeException("Value is not a number")
    }
    fun asInteger(): Int = when (this) {
        is IntegerLiteralValue -> value
        else -> throw RuntimeException("Value is not an integer")
    }

    fun asString(): String = when (this) {
        is StringLiteralValue -> value
        else -> throw RuntimeException("Value is not a string")
    }

    fun asBoolean(): Boolean = when (this) {
        is BooleanLiteralValue -> value
        else -> throw RuntimeException("Value is not a boolean")
    }

    fun isTruthy(): Boolean = when (this) {
        is BooleanLiteralValue -> value
        is NilLiteralValue -> false
        else -> true
    }

    fun isFalsy(): Boolean = !isTruthy()

    override fun toString(): String = when (this) {
            is DoubleLiteralValue -> value.toString()
            is IntegerLiteralValue -> value.toString()
            is StringLiteralValue -> value
            is BooleanLiteralValue -> value.toString()
            NilLiteralValue -> "nil"
            is IdentifierLiteralValue -> name
            is NativeFunctionLiteralValue -> "<fn $name>"
            is FunctionLiteralValue -> "<fn $name>"
        }

}
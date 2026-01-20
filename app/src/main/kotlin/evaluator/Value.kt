package evaluator

sealed class Value {
    class NumberValue(val value: Double) : Value()
    class StringValue(val value: String) : Value()
    class BooleanValue(val value: Boolean) : Value()
    object NilValue : Value()
    class IdentifierValue(val name: String) : Value()

    fun asNumber(): Double = when (this) {
        is Value.NumberValue -> value
        else -> throw RuntimeException("Value is not a number")
    }

    fun asString(): String = when (this) {
        is Value.StringValue -> value
        else -> throw RuntimeException("Value is not a string")
    }

    fun asBoolean(): Boolean = when (this) {
        is Value.BooleanValue -> value
        else -> throw RuntimeException("Value is not a boolean")
    }

    override fun toString(): String = when (this) {
            is Value.NumberValue -> value.toString()
            is Value.StringValue -> value
            is Value.BooleanValue -> value.toString()
            Value.NilValue -> "nil"
            is Value.IdentifierValue -> name
        }

}
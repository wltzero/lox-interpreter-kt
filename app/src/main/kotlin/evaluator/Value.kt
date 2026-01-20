package evaluator

sealed class Value {
    class DoubleValue(val value: Double) : Value()
    class IntegerValue(val value: Int) : Value()
    class StringValue(val value: String) : Value()
    class BooleanValue(val value: Boolean) : Value()
    object NilValue : Value()
    class IdentifierValue(val name: String) : Value()

    fun asDouble(): Double = when (this) {
        is DoubleValue -> value
        else -> throw RuntimeException("Value is not a number")
    }
    fun asInteger(): Int = when (this) {
        is IntegerValue -> value
        else -> throw RuntimeException("Value is not an integer")
    }

    fun asString(): String = when (this) {
        is StringValue -> value
        else -> throw RuntimeException("Value is not a string")
    }

    fun asBoolean(): Boolean = when (this) {
        is BooleanValue -> value
        else -> throw RuntimeException("Value is not a boolean")
    }

    override fun toString(): String = when (this) {
            is DoubleValue -> value.toString()
            is IntegerValue -> value.toString()
            is StringValue -> value
            is BooleanValue -> value.toString()
            NilValue -> "nil"
            is IdentifierValue -> name
        }

}
package statement

import evaluator.LiteralValue

data class VariableValue(
    val name: String, val value: LiteralValue
)
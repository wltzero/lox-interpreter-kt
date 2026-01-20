package evaluator

import parser.ASTNode
import tokenizer.TokenType


class EvaluateVisitor : ASTNode.ASTVisitor<Value> {
    private val environment = mutableMapOf<String, Value>()

    fun evaluate(node: ASTNode): Value {
        return node.accept(this)
    }


    override fun visitBinaryExp(exp: ASTNode.BinaryExp): Value {
        val leftValue = exp.left.accept(this)
        val rightValue = exp.right.accept(this)

        return when (exp.op) {
            TokenType.PLUS -> handlePlus(leftValue, rightValue)
            TokenType.MINUS -> handleMinus(leftValue, rightValue)
            TokenType.STAR -> handleStar(leftValue, rightValue)
            TokenType.SLASH -> handleSlash(leftValue, rightValue)

            TokenType.MOD -> Value.DoubleValue(leftValue.asDouble() % rightValue.asDouble())

            TokenType.LESS -> Value.BooleanValue(leftValue.asDouble() < rightValue.asDouble())
            TokenType.LESS_EQUAL -> Value.BooleanValue(leftValue.asDouble() <= rightValue.asDouble())
            TokenType.GREATER -> Value.BooleanValue(leftValue.asDouble() > rightValue.asDouble())
            TokenType.GREATER_EQUAL -> Value.BooleanValue(leftValue.asDouble() >= rightValue.asDouble())
            TokenType.EQUAL_EQUAL -> Value.BooleanValue(areValuesEqual(leftValue, rightValue))
            TokenType.BANG_EQUAL -> Value.BooleanValue(!areValuesEqual(leftValue, rightValue))

            TokenType.AND -> Value.BooleanValue(leftValue.asBoolean() && rightValue.asBoolean())
            TokenType.OR -> Value.BooleanValue(leftValue.asBoolean() || rightValue.asBoolean())

            else -> throw RuntimeException("Unsupported binary operator: ${exp.op}")
        }
    }

    private fun handlePlus(left: Value, right: Value): Value {
        return when {
            left is Value.DoubleValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value + right.value)
            }
            left is Value.IntegerValue && right is Value.IntegerValue -> {
                Value.IntegerValue(left.value + right.value)
            }
            left is Value.IntegerValue && right is Value.DoubleValue-> {
                Value.DoubleValue(left.value + right.value)
            }
            left is Value.DoubleValue && right is Value.IntegerValue -> {
                Value.DoubleValue(left.value + right.value)
            }
            left is Value.StringValue && right is Value.StringValue -> {
                Value.StringValue(left.value + right.value)
            }
            else -> throw RuntimeException("Operands must be two numbers or two strings.")
        }
    }

    private fun handleMinus(left: Value, right: Value): Value {
        return when {
            left is Value.DoubleValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value - right.value)
            }
            left is Value.IntegerValue && right is Value.IntegerValue -> {
                Value.IntegerValue(left.value - right.value)
            }
            left is Value.IntegerValue && right is Value.DoubleValue-> {
                Value.DoubleValue(left.value - right.value)
            }
            left is Value.DoubleValue && right is Value.IntegerValue -> {
                Value.DoubleValue(left.value - right.value)
            }
            else -> throw RuntimeException("Operands must be numbers.")
        }
    }

    private fun handleStar(left: Value, right: Value): Value {
        return when {
            left is Value.DoubleValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value * right.value)
            }
            left is Value.IntegerValue && right is Value.IntegerValue -> {
                Value.IntegerValue(left.value * right.value)
            }
            left is Value.IntegerValue && right is Value.DoubleValue-> {
                Value.DoubleValue(left.value * right.value)
            }
            left is Value.DoubleValue && right is Value.IntegerValue -> {
                Value.DoubleValue(left.value * right.value)
            }
            else -> throw RuntimeException("Operands must be numbers.")
        }
    }
    private fun handleSlash(left: Value, right: Value): Value {
        return when {
            right is Value.DoubleValue && right.value == 0.0 -> throw RuntimeException("Division by zero")
            right is Value.IntegerValue && right.value == 0 -> throw RuntimeException("Division by zero")

            left is Value.DoubleValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value / right.value)
            }
            left is Value.IntegerValue && right is Value.IntegerValue -> {
                if (left.value % right.value == 0) {
                    Value.IntegerValue(left.value / right.value)
                } else {
                    Value.DoubleValue(left.value.toDouble() / right.value.toDouble())
                }
            }
            left is Value.IntegerValue && right is Value.DoubleValue-> {
                Value.DoubleValue(left.value / right.value)
            }
            left is Value.DoubleValue && right is Value.IntegerValue -> {
                Value.DoubleValue(left.value / right.value)
            }
            else -> throw RuntimeException("Operands must be numbers.")
        }
    }


    // 2. 处理一元表达式
    override fun visitUnaryExp(exp: ASTNode.UnaryExp): Value {
        val operandValue = exp.operand.accept(this)
        return when (exp.op) {
            TokenType.MINUS -> when(operandValue){
                is Value.DoubleValue -> Value.DoubleValue(-operandValue.value)
                is Value.IntegerValue -> Value.IntegerValue(-operandValue.value)
                else -> throw RuntimeException("Operand must be a number.")
            }
            TokenType.PLUS -> operandValue
            TokenType.BANG -> when(operandValue){
                is Value.BooleanValue -> Value.BooleanValue(!operandValue.value)
                is Value.NilValue -> Value.BooleanValue(true)
                is Value.IntegerValue -> Value.BooleanValue(false)
                is Value.DoubleValue -> Value.BooleanValue(false)
                else -> throw RuntimeException("Unsupported operand type for unary bang: ${operandValue::class.simpleName}")
            }
            else -> throw RuntimeException("Unsupported unary operator: ${exp.op}")
        }
    }

    // 3. 处理分组表达式
    override fun visitGroupingExp(exp: ASTNode.GroupingExp): Value {
        return exp.expression.accept(this)
    }

    // 4. 处理标识符（变量）
    override fun visitIdentifierExp(exp: ASTNode.IdentifierExp): Value {
        return environment[exp.identifier] ?: throw RuntimeException("Undefined variable: ${exp.identifier}")
    }

    // 5. 处理字面量（直接返回对应 Value）
    override fun visitStringLiteral(exp: ASTNode.StringLiteral): Value {
        return Value.StringValue(exp.string)
    }

    override fun visitBooleanLiteral(exp: ASTNode.BooleanLiteral): Value {
        return Value.BooleanValue(exp.value)
    }

    override fun visitNilLiteral(exp: ASTNode.NilLiteral): Value {
        return Value.NilValue
    }

    override fun visiteDoubleLiteral(exp: ASTNode.DoubleLiteral): Value {
        return Value.DoubleValue(exp.number)
    }

    override fun visitIntegerLiteral(exp: ASTNode.IntegerLiteral): Value {
        return Value.IntegerValue(exp.number)
    }

    // 辅助方法：判断两个 Value 是否相等
    private fun areValuesEqual(a: Value, b: Value): Boolean {
        return when {
            a is Value.NilValue && b is Value.NilValue -> true
            a is Value.DoubleValue && b is Value.DoubleValue -> a.value == b.value
            a is Value.IntegerValue && b is Value.IntegerValue -> a.value == b.value
            a is Value.BooleanValue && b is Value.BooleanValue -> a.value == b.value
            a is Value.StringValue && b is Value.StringValue -> a.value == b.value
            else -> false
        }
    }

    fun assignVariable(name: String, value: Value) {
        environment[name] = value
    }
}
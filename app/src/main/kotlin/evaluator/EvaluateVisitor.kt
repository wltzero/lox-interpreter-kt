package evaluator

import parser.ASTNode
import tokenizer.TokenType


class EvaluateVisitor : ASTNode.ASTVisitor<Value> {
    private val environment = mutableMapOf<String, Value>()

    // 核心入口方法

    fun evaluate(node: ASTNode): Value {
        return node.accept(this)
    }


    override fun visitBinaryExp(exp: ASTNode.BinaryExp): Value {
        val leftValue = exp.left.accept(this)
        val rightValue = exp.right.accept(this)

        return when (exp.op) {
            TokenType.PLUS -> handlePlus(leftValue, rightValue)
            TokenType.MINUS -> Value.NumberValue(leftValue.asNumber() - rightValue.asNumber())
            TokenType.STAR -> Value.NumberValue(leftValue.asNumber() * rightValue.asNumber())
            TokenType.SLASH -> {
                if (rightValue.asNumber() == 0.0) throw RuntimeException("Division by zero")
                Value.NumberValue(leftValue.asNumber() / rightValue.asNumber())
            }
            TokenType.MOD -> Value.NumberValue(leftValue.asNumber() % rightValue.asNumber())

            TokenType.LESS -> Value.BooleanValue(leftValue.asNumber() < rightValue.asNumber())
            TokenType.LESS_EQUAL -> Value.BooleanValue(leftValue.asNumber() <= rightValue.asNumber())
            TokenType.GREATER -> Value.BooleanValue(leftValue.asNumber() > rightValue.asNumber())
            TokenType.GREATER_EQUAL -> Value.BooleanValue(leftValue.asNumber() >= rightValue.asNumber())
            TokenType.EQUAL_EQUAL -> Value.BooleanValue(areValuesEqual(leftValue, rightValue))
            TokenType.BANG_EQUAL -> Value.BooleanValue(!areValuesEqual(leftValue, rightValue))

            TokenType.AND -> Value.BooleanValue(leftValue.asBoolean() && rightValue.asBoolean())
            TokenType.OR -> Value.BooleanValue(leftValue.asBoolean() || rightValue.asBoolean())

            else -> throw RuntimeException("Unsupported binary operator: ${exp.op}")
        }
    }

    // 处理 + 号重载（数字加法/字符串拼接）
    private fun handlePlus(left: Value, right: Value): Value {
        return when {
            left is Value.NumberValue && right is Value.NumberValue -> {
                Value.NumberValue(left.value + right.value)
            }
            left is Value.StringValue && right is Value.StringValue -> {
                Value.StringValue(left.value + right.value)
            }
            else -> throw RuntimeException("Unsupported + operation between ${left::class.simpleName} and ${right::class.simpleName}")
        }
    }

    // 2. 处理一元表达式
    override fun visitUnaryExp(exp: ASTNode.UnaryExp): Value {
        val operandValue = exp.operand.accept(this)
        return when (exp.op) {
            TokenType.MINUS -> Value.NumberValue(-operandValue.asNumber())
            TokenType.PLUS -> operandValue
            TokenType.BANG -> Value.BooleanValue(!operandValue.asBoolean())
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

    override fun visitNumberExp(exp: ASTNode.NumberExp): Value {
        return Value.NumberValue(exp.number)
    }

    // 辅助方法：判断两个 Value 是否相等
    private fun areValuesEqual(a: Value, b: Value): Boolean {
        return when {
            a is Value.NilValue && b is Value.NilValue -> true
            a is Value.NumberValue && b is Value.NumberValue -> a.value == b.value
            a is Value.BooleanValue && b is Value.BooleanValue -> a.value == b.value
            a is Value.StringValue && b is Value.StringValue -> a.value == b.value
            else -> false
        }
    }

    fun assignVariable(name: String, value: Value) {
        environment[name] = value
    }
}
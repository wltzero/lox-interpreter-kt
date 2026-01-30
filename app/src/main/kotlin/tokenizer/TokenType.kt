package tokenizer

enum class TokenType(val symbol: String? = null) {
    // 特殊标记
    EOF(null),                           // 文件结束
    STRING_UNTERMINATED(null),          // 未闭合字符串
    UNEXPECTED_CHAR(null),              // 意外字符

    // 注释
    COMMENT("//"),

    // 字面量
    STRING(null),
    NUMBER(null),
    IDENTIFIER(null),                   // 标识符

    // 关键字
    AND("and"),
    BREAK("break"),
    CLASS("class"),
    CONTINUE("continue"),
    ELSE("else"),
    FALSE("false"),
    FOR("for"),
    FUN("fun"),
    IF("if"),
    NIL("nil"),
    OR("or"),
    RETURN("return"),
    SUPER("super"),
    THIS("this"),
    TRUE("true"),
    VAR("var"),
    WHILE("while"),
    PRINT("print"),

    // 操作符
    // 一元运算符
    BANG("!"),                          // 感叹号 !
    MINUS("-"),                         // 负号 -
    PLUS("+"),                          // 正号 +
    // 二元运算符
    BANG_EQUAL("!="),                   // 不等于 !=
    EQUAL("="),                         // 赋值 =
    EQUAL_EQUAL("=="),                  // 等于 ==
    LESS("<"),                          // 小于<
    GREATER(">"),                       // 大于>
    LESS_EQUAL("<="),                   // 小于等于<=,
    GREATER_EQUAL(">="),                //大于等于>=
    STAR("*"),                          // 乘号 *
    MOD("%"),                           // 模运算 %
    SLASH("/"),                         // 斜杠 /

    // 分隔符
    COMMA(","),                         // 逗号 ,
    DOT("."),                           // 点 .
    SEMICOLON(";"),                     // 分号 ;
    LEFT_PAREN("("),                    // 左括号 (
    RIGHT_PAREN(")"),                   // 右括号 )
    LEFT_BRACKET("["),                  // 左方括号 [
    RIGHT_BRACKET("]"),                 // 右方括号 ]
    LEFT_BRACE("{"),                    // 左花括号 {
    RIGHT_BRACE("}"),                   // 右花括号 }

    // 空白字符
    SPACE(" "),                         // 空格
    NEWLINE("\n")                       // 换行符
}
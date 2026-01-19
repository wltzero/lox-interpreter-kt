package tokenizer

enum class TokenType(val symbol: String? = null) {
    EOF(null),                           // 文件结束
    STRING_UNTERMINATED(null),          // 未闭合字符串
    UNEXPECTED_CHAR(null),              // 意外字符

    COMMENT("//"),
    STRING(null),
    AND("and"),
    VAR("var"),
    CLASS("class"),
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
    WHILE("while"),
    PRINT("print"),
    IDENTIFIER(null),                   // 标识符
    NUMBER(null),                       // 数字
    SPACE(" "),                         // 空格
    SLASH("/"),                         // 斜杠 /
    NEWLINE("\n"),                      // 换行符
    BANG("!"),                          // 感叹号 !
    BANG_EQUAL("!="),                   // 不等于 !=
    EQUAL_EQUAL("=="),                  // 等于 ==
    LESS("<"),                          // 小于<
    GREATER(">"),                       // 大于>
    LESS_EQUAL("<="),                   // 小于等于<=,
    GREATER_EQUAL(">="),                //大于等于>=
    EQUAL("="),                         // 赋值 =
    COMMA(","),                         // 逗号 ,
    DOT("."),                           // 点 .
    MINUS("-"),                         // 减号 -
    PLUS("+"),                          // 加号 +
    STAR("*"),                          // 乘号 *
    MOD("%"),                           // 乘号 *
    LEFT_PAREN("("),                    // 左括号 (
    RIGHT_PAREN(")"),                   // 右括号 )
    LEFT_BRACKET("["),                  // 左方括号 [
    RIGHT_BRACKET("]"),                 // 右方括号 ]
    LEFT_BRACE("{"),                    // 左花括号 {
    RIGHT_BRACE("}"),                   // 右花括号 }
    SEMICOLON(";")                      // 分号 ;
}
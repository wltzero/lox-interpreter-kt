package tokenizer

data class ParsedToken(
    val token: TokenType,
    val stringValue: String,
    val value: Any?,
    val line: Int
)

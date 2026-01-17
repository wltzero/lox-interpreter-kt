sealed class TokenMatcher {
    abstract fun match(t: String, start: Int): Int?
}

class RegexMatcher(val regex: Regex): TokenMatcher() {
    override fun match(t: String, start: Int): Int? {
        val matchResult = regex.find(t, start)
        return if (matchResult != null && matchResult.range.first == start) {
            matchResult.range.last - matchResult.range.first + 1
        } else {
            null
        }
    }
}

class CharMatcher(val ch: Char) : TokenMatcher() {
    override fun match(t: String, start: Int): Int? {
        return if (start < t.length && t[start] == ch) {
            1
        } else {
            null
        }
    }
}

class EofMatcher : TokenMatcher() {
    override fun match(t: String, start: Int): Int? {
        return if (start >= t.length) {
            0
        } else {
            null
        }
    }
}

class Token(val name: String, val matcher: TokenMatcher) {
    constructor(name: String, ch: Char): this(name, CharMatcher(ch))
    constructor(name: String, s: String): this(name, RegexMatcher(Regex(s)))

    override fun toString(): String {
        return name
    }
}

private val eof = Token("EOF", EofMatcher())
private val tokens = listOf(
    Token("COMMENT", "//.*"),
    Token("STRING", "\"([^\"\\\\]|\\\\.)*\""),
    Token("STRING_UNTERMINATED", "\"([^\"\\\\]|\\\\.)*"),
    Token("NUMBER", "\\d+(\\.\\d+)?"),

    Token("IDENTIFIER", "[a-zA-Z_][a-zA-Z0-9_]*"),
    Token("AND", "and"),
    Token("CLASS", "class"),
    Token("ELSE", "else"),
    Token("FALSE", "false"),
    Token("FOR", "for"),
    Token("FUN", "fun"),
    Token("IF", "if"),
    Token("NIL", "nil"),
    Token("OR", "or"),
    Token("PRINT", "print"),
    Token("RETURN", "return"),
    Token("SUPER", "super"),
    Token("THIS", "this"),
    Token("TRUE", "true"),
    Token("VAR", "var"),
    Token("WHILE", "while"),

    Token("SPACES", "[ \t]+"),
    Token("NEWLINES", "[\r?\n]+"),
    Token("SLASH", '/'),
    Token("LESS_EQUAL", "<="),
    Token("GREATER_EQUAL", ">="),
    Token("LESS", "<"),
    Token("GREATER", ">"),
    Token("BANG_EQUAL", "!="),
    Token("BANG", "!"),
    Token("EQUAL_EQUAL", "=="),
    Token("EQUAL", '='),
    Token("COMMA", ','),
    Token("DOT", '.'),
    Token("MINUS", '-'),
    Token("PLUS", '+'),
    Token("SEMICOLON", ';'),
    Token("SLASH", '/'),
    Token("STAR", '*'),
    Token("LEFT_PAREN", '('),
    Token("RIGHT_PAREN", ')'),
    Token("LEFT_BRACE", '{'),
    Token("RIGHT_BRACE", '}'),
    eof
)

sealed class ParsedToken {
    class MatchedSimpleToken(val t: Token, val match: String) : ParsedToken()
    class MatchedVariableToken(val t: Token, val match: String, val literal: Any) : ParsedToken()
    class UnexpectedChar(val line: Int, val ch: Char) : ParsedToken()
    class UnterminatedString(val line: Int): ParsedToken()
}

fun parseContent(text: String): List<ParsedToken> {
    var pos = 0
    val result = mutableListOf<ParsedToken>()
    var line = 1

    while (pos < text.length) {
        // 找到下一个符合规则的token
        val match = tokens.firstNotNullOfOrNull { token -> token.matcher.match(text, pos)?.let { token to it } }
        when {
            match == null -> {
                result.add(ParsedToken.UnexpectedChar(line, text[pos]))
                pos++
            }
            match.first.name=="COMMENT" || match.first.name=="SPACES" ->{
                pos += match.second
            }
            match.first.name=="NEWLINES" ->{
                pos += match.second
                line++
            }
            match.first.name=="NUMBER" ->{
                result.add(ParsedToken.MatchedVariableToken(match.first, text.substring(pos, pos + match.second), text.substring(pos, pos + match.second).toDouble()))
                pos += match.second
            }
            match.first.name=="STRING" ->{
                result.add(ParsedToken.MatchedVariableToken(match.first, text.substring(pos, pos + match.second), text.substring(pos, pos + match.second).removeSurrounding("\"")))
                pos += match.second
            }
            match.first.name=="STRING_UNTERMINATED" ->{
                result.add(ParsedToken.UnterminatedString(line))
                pos += match.second
            }
            else -> {
                val (token, matchedLength) = match
                // 前进matchedLength个字符
                result.add(ParsedToken.MatchedSimpleToken(token, text.substring(pos, pos + matchedLength)))
                pos += matchedLength
            }
        }
    }
    result.add(ParsedToken.MatchedSimpleToken(eof,""))
    return result
}
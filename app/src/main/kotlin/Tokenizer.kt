sealed class TokenMatcher {
    abstract fun match(t: String, start: Int): Int?
}

class RegexMatcher(val regex: Regex) : TokenMatcher() {
    override fun match(t: String, start: Int): Int? {
        val matchResult = regex.find(t, start)
        return if (matchResult != null && matchResult.range.first == start) {
            matchResult.range.last - matchResult.range.first
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
    constructor(name: String, ch: Char) : this(name, CharMatcher(ch))

    override fun toString(): String {
        return name
    }
}

private val eof = Token("EOF", EofMatcher())
private val tokens = listOf(
    Token("COMMA", ','), Token("DOT", '.'), Token("MINUS", '-'), Token("PLUS", '+'), Token("SEMICOLON", ';'), Token("SLASH", '/'), Token("STAR", '*'), Token("LEFT_PAREN", '('), Token("RIGHT_PAREN", ')'), Token("LEFT_BRACE", '{'), Token("RIGHT_BRACE", '}'), eof
)

sealed class ParsedToken {
    class MatchedToken(val t: Token, val match: String) : ParsedToken()
    class UnexpectedChar(val line: Int, val ch: Char) : ParsedToken()
}

fun parseContent(text: String): List<ParsedToken> {
    var pos = 0
    var result = mutableListOf<ParsedToken>()
    var line = 1

    while (pos < text.length) {
        // 找到下一个符合规则的token
        val matches = tokens.mapNotNull { token -> token.matcher.match(text, pos)?.let { token to it } }
        when {
            matches.size > 1 -> throw RuntimeException("founded multiple matches")
            matches.isEmpty() -> {
                result.add(ParsedToken.UnexpectedChar(line, text[pos]))
                pos++
            }

            else -> {
                val (token, matchedLength) = matches[0]
                // 前进matchedLength个字符
                result.add(ParsedToken.MatchedToken(token, text.substring(pos, pos + matchedLength)))
                pos += matchedLength
            }
        }
    }
    result.add(ParsedToken.MatchedToken(eof,""))
    return result
}
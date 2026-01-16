import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.err.println("Logs from your program will appear here!")

    if (args.size < 2) {
        System.err.println("Usage: ./your_program.sh tokenize <filename>")
        exitProcess(1)
    }

    val command = args[0]
    val filename = args[1]

    if (command != "tokenize") {
        System.err.println("Unknown command: $command")
        exitProcess(1)
    }

    val fileContents = File(filename).readText()

    if (fileContents.isNotEmpty()) {
        parse(fileContents)
    } else {
        println("EOF  null")
    }
}

fun parse(text: String) = text.lineSequence().forEachIndexed { i, line ->
    line.forEach { char ->
        runCatching { LoxToken.fromString(char.toString()) }
            .onSuccess(::println)
            .onFailure { println("[line ${i + 1}] Error: Unexpected character: $char") }
    }
}.also { println("EOF  null") }


enum class LoxToken(val value: String) {
    LEFT_PAREN("("), RIGHT_PAREN(")"), LEFT_BRACE("{"), RIGHT_BRACE("}"), COMMA(","), DOT("."), MINUS("-"), PLUS("+"), SEMICOLON(";"), SLASH("/"), STAR("*"), BANG("!"), EOF("");

    override fun toString(): String {
        return "${this.name} ${this.value} null"
    }

    companion object {
        fun fromString(str: String): LoxToken {
            return entries.find { it.value == str } ?: throw InvalidTokenException("Unknown token: $str")
        }
    }

}

class InvalidTokenException(
    message: String
) : Exception()
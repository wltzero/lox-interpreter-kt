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

    when (command) {
        "tokenize" -> {
            val fileContents = File(filename).readText()
            val tokens = parseContent(fileContents)
            for (t in tokens) {
                when (t) {
                    is ParsedToken.MatchedSimpleToken -> println("${t.t.name} ${t.match} null")
                    is ParsedToken.MatchedStringToken -> println("${t.t.name} ${t.match} ${t.literal}")
                    is ParsedToken.UnexpectedChar -> System.err.println("[line ${t.line}] Error: Unexpected character: ${t.ch}")
                    is ParsedToken.UnterminatedString -> System.err.println("[line ${t.line}] Error: Unterminated string.")
                }
            }

            if (tokens.any { it is ParsedToken.UnexpectedChar } || tokens.any { it is ParsedToken.UnterminatedString }) {
                exitProcess(65)
            }
        }
        else -> {
            System.err.println("Unknown command: ${command}")
            exitProcess(1)
        }
    }
}

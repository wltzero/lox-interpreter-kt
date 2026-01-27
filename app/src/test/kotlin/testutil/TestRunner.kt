package testutil

import cli.EvaluateCli
import cli.ParserCli
import cli.StatementCli
import cli.TokenizerCli
import tokenizer.TokenizerCliStatus
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * Lox脚本测试运行器
 * 提供统一的接口来执行Lox脚本并捕获输出结果
 */
class TestRunner {
    
    /**
     * 执行Lox脚本并返回结果
     * @param script 要执行的Lox脚本内容
     * @param command 执行命令 (tokenize|parse|evaluate|run)
     * @return 执行结果
     */
    fun runScript(script: String, command: String = "run"): ScriptExecutionResult {
        val stdout = ByteArrayOutputStream()
        val stderr = ByteArrayOutputStream()
        
        val originalOut = System.out
        val originalErr = System.err
        
        try {
            System.setOut(PrintStream(stdout))
            System.setErr(PrintStream(stderr))
            
            val exitCode = when (command) {
                "tokenize" -> {
                    val status = TokenizerCli.tokenize(script, System.out, System.err)
                    if (status == TokenizerCliStatus.ERROR) 65 else 0
                }
                "parse" -> {
                    try {
                        ParserCli.parse(script, System.out, System.err)
                        0
                    } catch (e: Exception) {
                        System.err.println(e.message)
                        65
                    }
                }
                "evaluate" -> {
                    try {
                        EvaluateCli.evaluate(script, System.out, System.err)
                        0
                    } catch (e: Exception) {
                        System.err.println(e.message)
                        70
                    }
                }
                "run" -> {
                    try {
                        StatementCli.doRun(script, System.out, System.err)
                        0
                    } catch (e: Exception) {
                        System.err.println(e.message)
                        when (e::class.simpleName) {
                            "ParserException" -> 65
                            "ResolutionException" -> 65
                            "EvaluateException" -> 70
                            "VariableNotFoundException" -> 70
                            else -> 1
                        }
                    }
                }
                else -> {
                    System.err.println("Unknown command: $command")
                    1
                }
            }
            
            return ScriptExecutionResult(
                exitCode = exitCode,
                stdout = stdout.toString().trim(),
                stderr = stderr.toString().trim(),
                success = exitCode == 0
            )
            
        } finally {
            System.setOut(originalOut)
            System.setErr(originalErr)
        }
    }
    
    /**
     * 快速执行脚本的方法
     */
    fun tokenize(script: String) = runScript(script, "tokenize")
    fun parse(script: String) = runScript(script, "parse")
    fun evaluate(script: String) = runScript(script, "evaluate")
    fun run(script: String) = runScript(script, "run")
}

/**
 * 脚本执行结果数据类
 */
data class ScriptExecutionResult(
    val exitCode: Int,
    val stdout: String,
    val stderr: String,
    val success: Boolean
) {
    override fun toString(): String {
        return """
            ScriptExecutionResult(
                exitCode=$exitCode,
                success=$success,
                stdout="$stdout",
                stderr="$stderr"
            )
        """.trimIndent()
    }
}
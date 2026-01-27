package testutil

import org.junit.jupiter.api.Assertions.*
import kotlin.test.assertTrue

/**
 * Lox脚本测试专用断言工具
 */
object LoxAssertions {
    
    /**
     * 断言脚本执行成功
     */
    fun assertSuccess(result: ScriptExecutionResult, message: String = "Script should execute successfully") {
        assertTrue(result.success, "$message\n$result")
    }
    
    /**
     * 断言脚本执行失败
     */
    fun assertFailure(result: ScriptExecutionResult, expectedExitCode: Int? = null, message: String = "Script should fail") {
        assertFalse(result.success, "$message\n$result")
        if (expectedExitCode != null) {
            assertEquals(expectedExitCode, result.exitCode, "Expected exit code $expectedExitCode but got ${result.exitCode}")
        }
    }
    
    /**
     * 断言标准输出包含特定文本
     */
    fun assertOutputContains(result: ScriptExecutionResult, expectedSubstring: String, message: String = "") {
        assertTrue(
            result.stdout.contains(expectedSubstring),
            "${message}Expected output to contain '$expectedSubstring' but got:\n${result.stdout}"
        )
    }
    
    /**
     * 断言标准输出等于期望值
     */
    fun assertOutputEquals(result: ScriptExecutionResult, expectedOutput: String, message: String = "") {
        assertEquals(
            expectedOutput.trim(),
            result.stdout.trim(),
            "${message}Expected output:\n$expectedOutput\nBut got:\n${result.stdout}"
        )
    }
    
    /**
     * 断言标准错误包含特定文本
     */
    fun assertErrorContains(result: ScriptExecutionResult, expectedError: String, message: String = "") {
        assertTrue(
            result.stderr.contains(expectedError),
            "${message}Expected error to contain '$expectedError' but got:\n${result.stderr}"
        )
    }
    
    /**
     * 断言数值计算结果正确
     */
    fun assertNumericResult(result: ScriptExecutionResult, expectedValue: Double, tolerance: Double = 0.0001) {
        val actualValue = result.stdout.trim().toDoubleOrNull()
        assertNotNull(actualValue, "Output should be a valid number but got: ${result.stdout}")
        
        assertEquals(
            expectedValue,
            actualValue!!,
            tolerance,
            "Expected numeric result $expectedValue but got $actualValue"
        )
    }
    
    /**
     * 断言布尔结果正确
     */
    fun assertBooleanResult(result: ScriptExecutionResult, expectedValue: Boolean) {
        val actualValue = result.stdout.trim().toBooleanStrictOrNull()
        assertNotNull(actualValue, "Output should be a valid boolean but got: ${result.stdout}")
        
        assertEquals(
            expectedValue,
            actualValue!!,
            "Expected boolean result $expectedValue but got $actualValue"
        )
    }
    
    /**
     * 断言字符串结果正确
     */
    fun assertStringResult(result: ScriptExecutionResult, expectedValue: String) {
        assertEquals(
            expectedValue,
            result.stdout.trim(),
            "Expected string result '$expectedValue' but got '${result.stdout.trim()}'"
        )
    }
    
    /**
     * 断言输出行数
     */
    fun assertOutputLineCount(result: ScriptExecutionResult, expectedLineCount: Int, message: String = "") {
        val actualLineCount = if (result.stdout.isEmpty()) 0 else result.stdout.lines().size
        assertEquals(
            expectedLineCount,
            actualLineCount,
            "${message}Expected $expectedLineCount lines but got $actualLineCount lines"
        )
    }
    
    /**
     * 断言没有错误输出
     */
    fun assertNoErrors(result: ScriptExecutionResult, message: String = "") {
        assertTrue(
            result.stderr.isEmpty(),
            "${message}Expected no errors but got:\n${result.stderr}"
        )
    }
}
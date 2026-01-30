package resolver

import exception.ResolutionException
import parser.ASTNode

class Resolver {
    private val scopeStack = mutableListOf<MutableMap<String, Boolean>>()
    private val locals = mutableMapOf<ASTNode.Expr, Int>()
    private var currentFunction = FunctionType.NONE
    private var currentClass = ClassType.NONE

    private enum class FunctionType {
        NONE,
        FUNCTION,
        METHOD,
        INITIALIZER
    }

    private enum class ClassType {
        NONE,
        CLASS,
        SUBCLASS
    }

    fun resolve(statements: List<ASTNode.Stmt>) {
        statements.forEach { resolveStmt(it) }
    }

    fun getLocals(): Map<ASTNode.Expr, Int> = locals

    private fun resolveStmt(stmt: ASTNode.Stmt) {
        when (stmt) {
            is ASTNode.Stmt.ExpressionStmt -> resolveExpr(stmt.expression)
            is ASTNode.Stmt.PrintStmt -> resolveExpr(stmt.expression)
            is ASTNode.Stmt.VarStmt -> resolveVarStmt(stmt)
            is ASTNode.Stmt.BlockStmt -> resolveBlock(stmt.statements)
            is ASTNode.Stmt.IfStmt -> resolveIfStmt(stmt)
            is ASTNode.Stmt.ElseIfStmt -> resolveElseIfStmt(stmt)
            is ASTNode.Stmt.WhileStmt -> resolveWhileStmt(stmt)
            is ASTNode.Stmt.ForStmt -> resolveForStmt(stmt)
            is ASTNode.Stmt.FunctionStmt -> resolveFunctionStmt(stmt)
            is ASTNode.Stmt.ReturnStmt -> resolveReturnStmt(stmt)
            is ASTNode.Stmt.BreakStmt -> Unit  // break 语句不需要特殊解析
            is ASTNode.Stmt.ContinueStmt -> Unit  // continue 语句不需要特殊解析
            is ASTNode.Stmt.ClassStmt -> resolveClassStmt(stmt)
        }
    }

    private fun resolveExpr(expr: ASTNode.Expr) {
        when (expr) {
            is ASTNode.Expr.BinaryExp -> {
                resolveExpr(expr.left)
                resolveExpr(expr.right)
            }
            is ASTNode.Expr.UnaryExp -> resolveExpr(expr.operand)
            is ASTNode.Expr.GroupingExp -> resolveExpr(expr.expression)
            is ASTNode.Expr.IdentifyExp -> resolveVariable(expr)
            is ASTNode.Expr.AssignExp -> resolveAssign(expr)
            is ASTNode.Expr.CallExp -> {
                resolveExpr(expr.callee)
                expr.arguments.forEach { resolveExpr(it) }
            }
            is ASTNode.Expr.GetExpr -> resolveExpr(expr.obj)
            is ASTNode.Expr.SetExpr -> {
                resolveExpr(expr.obj)
                resolveExpr(expr.value)
            }
            is ASTNode.Expr.ThisExpr -> resolveThis(expr)
            is ASTNode.Expr.SuperExpr -> resolveSuper(expr)
            is ASTNode.Expr.StringLiteral,
            is ASTNode.Expr.BooleanLiteral,
            is ASTNode.Expr.NilLiteral,
            is ASTNode.Expr.DoubleLiteral,
            is ASTNode.Expr.IntegerLiteral -> Unit
        }
    }

    private fun resolveThis(expr: ASTNode.Expr.ThisExpr) {
        if (currentClass == ClassType.NONE) {
            throw ResolutionException("Can't use 'this' outside of a class.")
        }
        resolveLocal(expr, "this")
    }

    private fun resolveSuper(expr: ASTNode.Expr.SuperExpr) {
        if (currentClass == ClassType.NONE) {
            throw ResolutionException("Can't use 'super' outside of a class.")
        } else if (currentClass != ClassType.SUBCLASS) {
            throw ResolutionException("Can't use 'super' in a class with no superclass.")
        }
        resolveLocal(expr, "super")
    }

    private fun resolveClassStmt(stmt: ASTNode.Stmt.ClassStmt) {
        declare(stmt.name)
        define(stmt.name)

        val enclosingClass = currentClass
        if (stmt.superclass != null) {
            if (stmt.superclass.identifier == stmt.name) {
                throw ResolutionException("[line ${stmt.superclass.line}] A class can't inherit from itself.")
            }
            currentClass = ClassType.SUBCLASS
            resolveExpr(stmt.superclass)
        } else {
            currentClass = ClassType.CLASS
        }

        beginScope()
        scopeStack.last()["this"] = true

        if (stmt.superclass != null) {
            beginScope()
            scopeStack.last()["super"] = true
        }

        for (method in stmt.methods) {
            resolveFunction(method.name, method.parameters, method.body)
        }

        if (stmt.superclass != null) {
            endScope()
        }

        endScope()
        currentClass = enclosingClass
    }

    private fun resolveVarStmt(stmt: ASTNode.Stmt.VarStmt) {
        declare(stmt.name)
        resolveExpr(stmt.expression)
        define(stmt.name)
    }

    private fun resolveBlock(statements: List<ASTNode.Stmt>) {
        beginScope()
        resolve(statements)
        endScope()
    }

    private fun resolveIfStmt(stmt: ASTNode.Stmt.IfStmt) {
        resolveExpr(stmt.condition)
        stmt.thenBranch.forEach { resolveStmt(it) }
        stmt.elifBranches?.forEach { resolveStmt(it) }
        stmt.elseBranch?.forEach { resolveStmt(it) }
    }

    private fun resolveElseIfStmt(stmt: ASTNode.Stmt.ElseIfStmt) {
        resolveExpr(stmt.condition)
        stmt.thenBranch.forEach { resolveStmt(it) }
    }

    private fun resolveWhileStmt(stmt: ASTNode.Stmt.WhileStmt) {
        resolveExpr(stmt.condition)
        stmt.thenBranch.forEach { resolveStmt(it) }
    }

    private fun resolveForStmt(stmt: ASTNode.Stmt.ForStmt) {
        beginScope()
        stmt.initializer?.let { resolveStmt(it) }
        stmt.condition?.let { resolveExpr(it) }
        stmt.increment?.let { resolveExpr(it) }
        
        // 为循环体创建嵌套作用域
        beginScope()
        stmt.body.forEach { resolveStmt(it) }
        endScope()
        
        endScope()
    }

    private fun resolveFunctionStmt(stmt: ASTNode.Stmt.FunctionStmt) {
        declare(stmt.name)
        define(stmt.name)
        resolveFunction(stmt.name, stmt.parameters, stmt.body)
    }

    private fun resolveFunction(name: String, parameters: List<String>, body: List<ASTNode.Stmt>) {
        val enclosingFunction = currentFunction
        currentFunction = if (name == "init") FunctionType.INITIALIZER else FunctionType.FUNCTION
        beginScope()
        parameters.forEach {
            declare(it)
            define(it)
        }
        if (currentClass != ClassType.NONE) {
            declare("this")
            define("this")
        }
        resolve(body)
        endScope()
        currentFunction = enclosingFunction
    }

    private fun resolveReturnStmt(stmt: ASTNode.Stmt.ReturnStmt) {
        when {
            currentFunction == FunctionType.NONE -> {
                throw ResolutionException("Can't return from top-level code.")
            }
            currentFunction == FunctionType.INITIALIZER && stmt.value !is ASTNode.Expr.NilLiteral -> {
                throw ResolutionException("Can't return a value from an initializer.")
            }
            else -> resolveExpr(stmt.value)
        }
    }

    private fun resolveVariable(expr: ASTNode.Expr.IdentifyExp) {
        if (scopeStack.isNotEmpty()) {
            val scope = scopeStack.last()
            if (scope[expr.identifier] == false) {
                throw ResolutionException("Can't read local variable in its own initializer.")
            }
        }
        resolveLocal(expr, expr.identifier)
    }

    private fun resolveAssign(expr: ASTNode.Expr.AssignExp) {
        resolveExpr(expr.value)
        resolveLocal(expr, expr.name)
    }

    private fun resolveLocal(expr: ASTNode.Expr, name: String) {
        for (i in scopeStack.size - 1 downTo 0) {
            if (scopeStack[i].containsKey(name)) {
                locals[expr] = scopeStack.size - 1 - i
                return
            }
        }
    }

    private fun beginScope() {
        scopeStack.add(mutableMapOf())
    }

    private fun endScope() {
        if (scopeStack.isNotEmpty()) {
            scopeStack.removeLast()
        }
    }

    private fun declare(name: String) {
        if (scopeStack.isEmpty()) {
            return
        }
        val scope = scopeStack.last()
        if (scope.containsKey(name)) {
            throw ResolutionException("Already a variable with this name in this scope.")
        }
        scope[name] = false
    }

    private fun define(name: String) {
        if (scopeStack.isEmpty()) {
            return
        }
        scopeStack.last()[name] = true
    }
}

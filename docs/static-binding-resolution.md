# 静态绑定解析（当前实现）

本文描述当前 resolver 如何进行变量的静态绑定解析，以及解析结果在求值阶段如何使用。

## 解析发生的位置

解析在 `StatementCli` 中、执行前进行，使用 `resolver.Resolver`。
它遍历 AST、维护作用域栈，并把每个变量表达式的词法距离记录到 `locals` 映射中：

- Key: `ASTNode.Expr`（标识符或赋值表达式）
- Value: `Int` 距离（向外走过的作用域层数）

该映射会传给 `EvaluateVisitor.setLocals(...)`。

相关代码：
- `app/src/main/kotlin/resolver/Resolver.kt`
- `app/src/main/kotlin/cli/StatementCli.kt`
- `app/src/main/kotlin/evaluator/EvaluateVisitor.kt`

## 作用域与声明/定义

resolver 维护：

- `scopeStack: List<MutableMap<String, Boolean>>`
- `Boolean` 为 `false` 表示“已声明未定义”，为 `true` 表示“已定义”

规则：

1) 进入块时 **新建作用域**，离开块时结束作用域。
2) 遇到 `var` / `fun` 名称时 **declare**（仅在本地作用域内）。
3) 初始化表达式解析完成后 **define**（`fun` 名称可立即 define）。
4) **禁止在初始化器中读取自己**：
   - 如果名字在当前最内层作用域存在且为 `false`，抛出：
     `ResolutionException("Can't read local variable in its own initializer.")`

## 函数解析

函数在新作用域中解析：

1) 在当前作用域 declare + define 函数名。
2) 进入函数作用域。
3) 对参数逐个 declare + define。
4) 解析函数体语句。
5) 退出函数作用域。

顶层代码中 `return` 会报错：
`ResolutionException("Can't return from top-level code.")`

## 距离的计算方式

对每个标识符或赋值表达式，从内到外搜索作用域。
找到名字后记录：

```
distance = (scopeStack.size - 1 - scopeIndex)
```

如果没有在任何本地作用域找到该名字，就视为全局变量，不记录距离。

### 检索与命中逻辑（解析阶段）

1) resolver 只在 `scopeStack` 中查找名字（从内层到外层）。
2) 一旦在某一层命中，就记录该表达式的 `distance`，并停止继续查找。
3) 如果 `scopeStack` 没有命中，则不记录距离，视为全局。

换句话说，解析阶段的“命中”仅限于本地作用域；全局不入栈，只在求值阶段直接访问。

## 求值阶段如何使用解析结果

求值时：

- 如果存在距离，使用 `GlobalEnvironment.getAt(...)` / `assignAt(...)`。
- 如果不存在距离，直接访问全局环境：
  `getGlobal(...)` / `assignGlobal(...)`

### 检索与命中逻辑（求值阶段）

1) 对变量读取/赋值表达式，先在 `locals` 中取距离。
2) 命中距离：用 `getAt/assignAt` 从当前环境向外走指定层数。
3) 未命中距离：直接访问全局环境，不做动态链搜索。

这样可以保证闭包绑定的是解析时的环境，而不是运行时的动态链。

相关代码：
- `app/src/main/kotlin/statement/GlobalEnvironment.kt`
- `app/src/main/kotlin/evaluator/EvaluateVisitor.kt`

### AST 实例一致性假设

当前实现假设“解析阶段记录的 AST 节点实例”和“求值阶段使用的 AST 节点实例”是同一批对象：

- `locals` 的 key 是 `ASTNode.Expr` 实例（对象引用），不是名字字符串。
- 解析与求值共用同一棵 AST，因此能直接命中 `locals`。
- 如果中途重建/复制 AST，`locals` 就会失效，需要重新解析或改用可序列化的 key。

## 逐步演示案例

### 案例 1：块内声明不影响已解析的闭包

示例程序：

```
var variable = "global";

{
  fun f() {
    print variable;
  }

  f(); // 期望 "global"

  var variable = "local";

  f(); // 仍然是 "global"
}
```

解析过程（简化）：

1) 顶层 `var variable = "global"`：
   - 没有本地作用域，不记录距离。
2) 进入块：
   - 新建作用域 S1。
3) `fun f()`：
   - 在 S1 declare/define `f`。
   - 进入函数作用域 S2。
   - 解析 `print variable`：
     - S2 没有，S1 也没有（此时还未声明 local），因此不记录距离。
     - `locals` 中不新增该表达式的条目，视为全局访问。
   - 退出函数作用域 S2。
4) 调用 `f()`：
   - `f` 在 S1，距离为 0，对应 `locals[IdentifyExp("f")] = 0`。
5) `var variable = "local"`：
   - 在 S1 declare/define。
6) 再次调用 `f()`：
   - `f` 内的 `variable` 依然没有距离，读全局。
   - `locals` 中仍没有这处 `IdentifyExp("variable")` 的条目。

结果：两次打印都是 `"global"`。



### 案例 2：函数参数遮蔽

示例程序：

```
var x = "global";

fun f(x) {
  print x;
}

f("param"); // 期望 "param"
```

解析过程（简化）：

1) 顶层 `var x = "global"`：
   - 全局声明，不记录距离。
2) `fun f(x)`：
   - 在当前作用域 declare/define `f`。
   - 进入函数作用域 S1。
   - 参数 `x` 在 S1 declare/define。
   - 解析 `print x`：
     - `x` 在 S1，记录距离 0。
     - `locals[IdentifyExp("x")] = 0`。
3) 调用 `f("param")`：
   - `print x` 使用 `getAt(0, "x")`，访问函数参数。

结果：打印 `"param"`，全局 `x` 被遮蔽。


### 案例 3：for 作用域与外层变量

示例程序：

```
var i = "outer";

for (var i = 0; i < 1; i = i + 1) {
  print i;
}

print i;
```

解析过程（简化）：

1) 顶层 `var i = "outer"`：
   - 全局声明，不记录距离。
2) `for (...)`：
   - 解析时先 beginScope，进入 for 作用域 S1。
   - 初始化器 `var i = 0`：
     - 在 S1 declare/define。
   - 条件与增量表达式中的 `i`：
     - 在 S1 命中，距离 0。
     - `locals` 中为这些 `IdentifyExp("i")` 记录距离 0。
   - 循环体内的 `print i`：
     - 在 S1 命中，距离 0。
     - `locals[IdentifyExp("i")] = 0`（对应循环体内这次使用）。
   - 结束 for 作用域。
3) 循环外 `print i`：
   - `i` 未在本地命中，视为全局。

结果：循环内打印 `0`，循环外打印 `"outer"`。


## 备注

- 解析时找不到本地作用域的名字，默认视为全局。
- 全局环境是环境栈的第一个 frame。
- for 语句解析时会创建新作用域。

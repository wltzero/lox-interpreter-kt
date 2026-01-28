# Lox 解释器 (Kotlin 实现)

这是一个用 Kotlin 实现的 Lox 语言解释器，基于 [Crafting Interpreters](https://craftinginterpreters.com/) 教程。

## 项目结构

```
codecrafters-interpreter-kotlin/
├── app/src/main/kotlin/
│   ├── tokenizer/           # 词法分析
│   ├── parser/              # 语法分析 (Pratt Parser)
│   ├── evaluator/           # 解释执行
│   ├── resolver/            # 静态绑定解析
│   ├── statement/           # 全局环境
│   ├── cli/                 # 命令行入口
│   ├── exception/           # 异常定义
│   ├── collections/         # 工具集合
│   └── App.kt               # 主程序入口
├── app/src/test/kotlin/     # 测试用例
└── docs/                    # 文档
```

## 核心特性

- **词法分析**: 完整的 Token 解析，支持字符串、数字、标识符、关键字
- **语法分析**: Pratt Parser 实现，支持完整的表达式解析
- **解释执行**: 基于 AST 的解释器
- **静态绑定**: 变量作用域的静态解析，记录词法距离
- **函数**: 支持函数定义、调用、参数、返回值
- **闭包**: 完整的闭包支持，捕获外部变量
- **类系统**: 完整的面向对象支持
- **继承**: 单继承、super 关键字
- **构造器**: init 方法作为构造函数

## 运算符优先级

从高到低：
1. 一元运算符: `!`, `+`, `-`
2. 乘除模: `*`, `/`, `%`
3. 加减: `+`, `-`
4. 比较: `<`, `<=`, `>`, `>=`
5. 相等: `==`, `!=`
6. 逻辑与或: `and`, `or`
7. 赋值: `=`

## 运行方式

```bash
# 编译项目
./gradlew build

# 运行解释器
./gradlew run --args="<lox文件路径>"

# 运行测试
./gradlew test
```

## 代码架构

### 词法分析 (Tokenizer)
将源代码转换为 Token 序列，识别关键字、标识符、字面量等。

### 语法分析 (Parser)
使用 Pratt Parser 将 Token 序列解析为 AST，支持运算符优先级和结合性。

### 静态解析 (Resolver)
在执行前遍历 AST，解析变量作用域，记录每个变量的词法距离。

### 解释执行 (Evaluator)
遍历 AST 执行代码，使用全局环境管理变量，支持闭包。

## 文档

- [静态绑定解析](docs/static-binding-resolution.md)
- [语法特性](docs/syntax.md)

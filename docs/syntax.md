# Lox 语言语法特性

本文档描述 Lox 语言的完整语法特性和使用方式。

## 变量声明

使用 `var` 关键字声明变量，可以选择初始化：

```lox
var a = 1;
var b = "hello";
var c; // 默认值为 nil
```

变量作用域为块级作用域（block scope），支持词法作用域（lexical scoping）。

## 数据类型

Lox 是动态类型语言，支持以下基本类型：

### 数字

支持整数和浮点数：

```lox
var integer = 42;
var floating = 3.14;
var negative = -10;
```

### 字符串

使用双引号包围：

```lox
var greeting = "Hello, World!";
var concat = "Hello" + " " + "World";
```

### 布尔值

```lox
var t = true;
var f = false;
```

### 空值

```lox
var n = nil;
```

## 运算符

### 算术运算符

```lox
var a = 10 + 5;  // 加法
var b = 10 - 5;  // 减法
var c = 10 * 5;  // 乘法
var d = 10 / 5;  // 除法
var e = 10 % 3;  // 模运算
var f = -5;      // 负号（一元运算符）
var g = +5;      // 正号（一元运算符）
```

### 比较运算符

```lox
var a = 1 == 1;  // 等于
var b = 1 != 2;  // 不等于
var c = 1 < 2;   // 小于
var d = 1 <= 1;  // 小于等于
var e = 2 > 1;   // 大于
var f = 2 >= 1;  // 大于等于
```

### 逻辑运算符

```lox
var a = true and false;  // 逻辑与
var b = true or false;   // 逻辑或
var c = !true;           // 逻辑非（一元运算符）
```

### 赋值运算符

```lox
var x = 10;
x = 20;        // 简单赋值
x = x + 1;     // 复合赋值
```

### 运算符优先级

从高到低：
1. 一元运算符：`!`、`+`（正号）、`-`（负号）
2. 乘除模：`*`、`/`、`%`
3. 加减：`+`、`-`
4. 比较：`<`、`<=`、`>`、`>=`
5. 相等：`==`、`!=`
6. 逻辑与：`and`
7. 逻辑或：`or`
8. 赋值：`=`

## 打印语句

使用 `print` 输出值到标准输出：

```lox
print "Hello";
print 42;
print true;
print x + y;
```

## 控制流

### 条件语句

支持 `if`、`else if`、`else`：

```lox
if (condition) {
    // 代码块
} else if (condition2) {
    // 另一个分支
} else {
    // 默认分支
}

// 单行形式
if (condition) print "yes";
```

### 循环语句

#### while 循环

```lox
var i = 0;
while (i < 10) {
    print i;
    i = i + 1;
}
```

#### for 循环

```lox
for (var i = 0; i < 10; i = i + 1) {
    print i;
}
```

for 循环会创建新的块级作用域：

```lox
var i = "outer";
for (var i = 0; i < 1; i = i + 1) {
    print i;  // 输出 0
}
print i;      // 输出 "outer"
```

## 代码块

使用大括号 `{}` 创建代码块：

```lox
{
    var x = 10;
    print x;  // 在块内可见
}
// print x;  // 错误：x 未定义
```

## 函数

### 函数定义

使用 `fun` 关键字定义函数：

```lox
fun sayHello(name) {
    print "Hello, " + name + "!";
}

fun add(a, b) {
    return a + b;
}
```

### 函数调用

```lox
sayHello("World");
var result = add(1, 2);
```

### 返回值

使用 `return` 语句返回值：

```lox
fun getValue() {
    return 42;
}
```

### 函数参数

支持多个参数和默认值：

```lox
fun greet(greeting, name) {
    print greeting + ", " + name + "!";
}
```

## 闭包

Lox 支持完整的闭包功能，函数可以捕获外部变量：

```lox
fun makeCounter() {
    var count = 0;
    fun counter() {
        count = count + 1;
        return count;
    }
    return counter;
}

var c = makeCounter();
print c();  // 输出 1
print c();  // 输出 2
print c();  // 输出 3
```

闭包捕获的是变量的引用，而非值：

```lox
var x = "global";
fun f() {
    print x;
}

{
    var x = "local";
    f();  // 输出 "global"（词法作用域绑定）
}
```

## 类

### 类声明

```lox
class Robot {
    // 方法定义
}
```

### 实例化

```lox
var r2d2 = Robot();
```

### 属性

实例属性可以动态添加：

```lox
r2d2.model = "Astromech";
r2d2.operational = true;
print r2d2.model;
```

### 方法

在类体内定义方法：

```lox
class Calculator {
    add(a, b) {
        return a + b;
    }
}

var calc = Calculator();
print calc.add(1, 2);  // 输出 3
```

### this 关键字

在方法内使用 `this` 引用当前实例：

```lox
class Counter {
    init(start) {
        this.count = start;
    }

    increment() {
        this.count = this.count + 1;
        return this.count;
    }
}

var c = Counter(0);
print c.increment();  // 输出 1
print c.increment();  // 输出 2
```

### 构造函数

使用 `init` 方法作为构造函数：

```lox
class Robot {
    init(model, function) {
        this.model = model;
        this.function = function;
    }
}

var r2d2 = Robot("R2-D2", "Astromech");
print r2d2.model;  // 输出 "R2-D2"
```

注意：`init` 方法不能返回值（`return this;` 会报错）。

## 继承

使用 `<` 运算符实现单继承：

```lox
class Animal {
    speak() {
        return "Animal sound";
    }
}

class Dog < Animal {
    speak() {
        return "Woof";
    }
}
```

子类可以访问父类的方法：

```lox
var dog = Dog();
print dog.speak();  // 输出 "Woof"
```

### super 关键字

使用 `super` 调用父类的方法：

```lox
class Animal {
    speak() {
        return "Animal";
    }
}

class Dog < Animal {
    speak() {
        return super.speak() + " Dog";
    }
}
```

`super` 只能在类方法内使用，且必须有父类：

```lox
class Child < Parent {
    method() {
        super.method();  // 调用父类方法
    }
}
```

## 静态绑定与作用域

Lox 使用词法作用域（lexical scoping），变量在使用前必须在当前或外层作用域内声明。

### 变量解析顺序

1. 在当前作用域查找
2. 向外层作用域逐层查找
3. 视为全局变量

### 静态绑定示例

```lox
var x = "global";

fun f() {
    print x;  // 输出 "global"
}

{
    var x = "local";
    f();      // 仍输出 "global"（闭包绑定的是外层作用域的 x）
}
```

### 循环变量作用域

```lox
var x = "outer";

for (var x = 0; x < 1; x = x + 1) {
    print x;  // 输出 0
}

print x;      // 输出 "outer"（循环变量遮蔽外层变量）
```

## 注释

使用 `//` 添加单行注释：

```lox
// 这是一个注释
var x = 10;  // 行尾注释
```

## 错误处理

### 运行时错误

```lox
// 未定义变量
print undefined_var;  // 运行时错误：Undefined variable 'undefined_var'

// 类型错误
var f = "not a function";
f();  // 运行时错误：Can only call functions or classes.

// 属性错误
var obj = nil;
obj.property;  // 运行时错误：Only instances have properties.
```

### 编译时错误

```lox
// 语法错误
var x = ;  // 解析错误

// 语义错误
class A < A {}  // 类不能继承自己
fun returnTopLevel() {
    return 1;  // 顶层代码不能有 return
}
```

## 完整示例

```lox
class Calculator {
    init(name) {
        this.name = name;
        this.history = nil;
    }

    add(a, b) {
        var result = a + b;
        this.history = this.name + ": " + a + " + " + b + " = " + result;
        return result;
    }

    getHistory() {
        return this.history;
    }
}

fun calculate() {
    var calc = Calculator("MyCalc");
    var sum = calc.add(1, 2);
    print sum;           // 输出 3
    print calc.getHistory();  // 输出 "MyCalc: 1 + 2 = 3"
}

calculate();
```

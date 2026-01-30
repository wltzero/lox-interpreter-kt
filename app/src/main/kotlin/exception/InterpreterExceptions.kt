package exception

import evaluator.LiteralValue

class VariableNotFoundException(message: String): RuntimeException(message)
class EvaluateException(message: String): RuntimeException(message)
class ParserException(message: String): RuntimeException(message)
class ResolutionException(message: String): RuntimeException(message)

class ReturnException(var value: LiteralValue): Exception()
class BreakException: Exception()
class ContinueException: Exception()

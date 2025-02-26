package dev.pedroayon.pdm05b_abpj.util

fun evaluateExpression(expression: String): String {
    return try {
        val result = evaluate(expression).toString()
        if (result.endsWith(".0"))
            return result.dropLast(2)
        result
    } catch (e: Exception) {
        "Error"
    }
}

fun evaluate(expression: String) : Double {
    var terms = expression.split("+", limit = 2)
    if (terms.size == 2) return evaluate(terms[0]) +  evaluate(terms[1])
    terms = expression.split("-", limit = 2)
    if (terms.size == 2) return evaluate(terms[0]) -  evaluate(terms[1])
    terms = expression.split("*", limit = 2)
    if (terms.size == 2) return evaluate(terms[0]) *  evaluate(terms[1])
    terms = expression.split("/", limit = 2)
    if (terms.size == 2) return evaluate(terms[0]) /  evaluate(terms[1])
    terms = expression.split("%", limit = 2)
    if (terms.size == 2) return evaluate(terms[0]) %  evaluate(terms[1])
    terms = expression.split("^", limit = 2)
    if (terms.size == 2) return Math.pow(evaluate(terms[0]), evaluate(terms[1]))
    return expression.toDouble()
}
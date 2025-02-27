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

fun evaluate(expression: String): Double {
    val exp = clearParenthesisWhileSolvingThem(expression) // Usa una variable mutable

    var terms = exp.split("+", limit = 2)
    if (terms.size == 2) return evaluate(terms[0]) + evaluate(terms[1])

    terms = exp.split("-", limit = 2)
    if (terms.size == 2) return evaluate(terms[0]) - evaluate(terms[1])

    terms = exp.split("*", limit = 2)
    if (terms.size == 2) return evaluate(terms[0]) * evaluate(terms[1])

    terms = exp.split("/", limit = 2)
    if (terms.size == 2) return evaluate(terms[0]) / evaluate(terms[1])

    terms = exp.split("%", limit = 2)
    if (terms.size == 2) return evaluate(terms[0]) % evaluate(terms[1])

    terms = exp.split("^", limit = 2)
    if (terms.size == 2) return Math.pow(evaluate(terms[0]), evaluate(terms[1]))

    return exp.toDouble()
}


fun clearParenthesisWhileSolvingThem(e: String): String {
    var i = 0
    var result = e
    while (i < e.length) {
        if (e[i] == '(') {
            var openParenthesisCount = 1
            val openingParenthesisIndex = i
            i++
            while (i < e.length && openParenthesisCount > 0) {
                if (e[i] == '(') openParenthesisCount++
                else if (e[i] == ')') openParenthesisCount--
                i++
            }
            if (i >= e.length && openParenthesisCount > 0) {
                throw Exception("Parenthesis doesn't match.")
            }
            val closingParenthesisIndex = i - 1
            val subExp = e.substring(openingParenthesisIndex + 1, closingParenthesisIndex)
            result = e.substring(0, openingParenthesisIndex) + evaluateExpression(subExp)+ e.substring(closingParenthesisIndex + 1, e.length)

        } else if (e[i] == ')') {
            throw Exception("Parenthesis doesn't match.")
        }
        i++
    }

    return result
}

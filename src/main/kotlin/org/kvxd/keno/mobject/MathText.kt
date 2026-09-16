package org.kvxd.keno.mobject

import org.kvxd.keno.geometry.Vec2

class MathText(
    val expression: String,
    style: TextStyle = TextStyle(),
    alignment: TextAlignment = TextAlignment.CENTER,
    position: Vec2 = Vec2.Zero,
    scale: Vec2 = Vec2(1.0, 1.0),
    rotationDegrees: Double = 0.0,
    opacity: Double = 1.0,
    zIndex: Int = 0,
) : Text(
    value = format(expression),
    style = style,
    alignment = alignment,
    position = position,
    scale = scale,
    rotationDegrees = rotationDegrees,
    opacity = opacity,
    zIndex = zIndex,
) {
    companion object {
        private val symbols = linkedMapOf(
            "\\infty" to "∞",
            "\\theta" to "θ",
            "\\lambda" to "λ",
            "\\gamma" to "γ",
            "\\Delta" to "Δ",
            "\\alpha" to "α",
            "\\beta" to "β",
            "\\sqrt" to "√",
            "\\times" to "×",
            "\\cdot" to "·",
            "\\sum" to "∑",
            "\\int" to "∫",
            "\\pi" to "π",
            "\\neq" to "≠",
            "\\le" to "≤",
            "\\ge" to "≥",
            "\\to" to "→",
        )
        private val superscript = mapOf(
            '0' to '⁰', '1' to '¹', '2' to '²', '3' to '³', '4' to '⁴',
            '5' to '⁵', '6' to '⁶', '7' to '⁷', '8' to '⁸', '9' to '⁹',
            '+' to '⁺', '-' to '⁻', '=' to '⁼', '(' to '⁽', ')' to '⁾',
            'i' to 'ⁱ', 'n' to 'ⁿ',
        )
        private val subscript = mapOf(
            '0' to '₀', '1' to '₁', '2' to '₂', '3' to '₃', '4' to '₄',
            '5' to '₅', '6' to '₆', '7' to '₇', '8' to '₈', '9' to '₉',
            '+' to '₊', '-' to '₋', '=' to '₌', '(' to '₍', ')' to '₎',
            'a' to 'ₐ', 'e' to 'ₑ', 'i' to 'ᵢ', 'j' to 'ⱼ', 'n' to 'ₙ', 'o' to 'ₒ',
            'r' to 'ᵣ', 'u' to 'ᵤ', 'v' to 'ᵥ', 'x' to 'ₓ',
        )

        private fun format(source: String): String {
            var result = symbols.entries.fold(source) { value, (command, symbol) -> value.replace(command, symbol) }
            result = replaceScript(result, '^', superscript)
            result = replaceScript(result, '_', subscript)
            return result.replace("{", "").replace("}", "")
        }

        private fun replaceScript(source: String, marker: Char, alphabet: Map<Char, Char>): String {
            val result = StringBuilder()
            var index = 0
            while (index < source.length) {
                if (source[index] != marker || index + 1 >= source.length) {
                    result.append(source[index++])
                    continue
                }
                val grouped = source[index + 1] == '{'
                val contentStart = index + if (grouped) 2 else 1
                val contentEnd = if (grouped) source.indexOf('}', contentStart).takeIf { it >= 0 } ?: source.length else contentStart + 1
                val content = source.substring(contentStart, contentEnd)
                if (content.all(alphabet::containsKey)) {
                    content.forEach { result.append(alphabet.getValue(it)) }
                } else {
                    result.append(marker).append(content)
                }
                index = contentEnd + if (grouped && contentEnd < source.length) 1 else 0
            }
            return result.toString()
        }
    }
}

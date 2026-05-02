package com.yourname.calculatorapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.math.*

class ScientificFragment : Fragment() {
    private var display = "0"
    private var expression = ""
    private var operator = ""
    private var firstNum = 0.0
    private var newInput = true
    private var openParens = 0
    private var isDeg = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_scientific, container, false)
        val tvDisplay = view.findViewById<TextView>(R.id.tvSciDisplay)
        val tvExpression = view.findViewById<TextView>(R.id.tvSciExpression)
        val grid = view.findViewById<GridLayout>(R.id.sciButtonGrid)

        val operators = listOf("÷", "×", "-", "+", "=")
        val sciButtons = listOf(
            "DEG", "sin", "cos", "tan",
            "ln", "log", "√", "^",
            "(", ")", "π", "e",
            "⌫", "±", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "EXP", "="
        )

        sciButtons.forEach { label ->
            val btn = Button(requireContext()).apply {
                text = label
                textSize = 16f
                val isSci = label in listOf("sin","cos","tan","ln","log","√","^","π","e","DEG","EXP")
                val isOp = label in operators
                setBackgroundColor(android.graphics.Color.parseColor(
                    when {
                        isSci -> "#2D1B4E"
                        isOp -> "#1A1A1A"
                        else -> "#111111"
                    }
                ))
                setTextColor(android.graphics.Color.WHITE)
                stateListAnimator = null
                val p = GridLayout.LayoutParams().apply {
                    width = 0; height = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f)
                    setMargins(4, 4, 4, 4)
                }
                layoutParams = p
            }
            btn.setOnClickListener { handleSci(label, tvDisplay, tvExpression, btn) }
            grid.addView(btn)
        }
        return view
    }

    private fun handleSci(label: String, tv: TextView, tvExpr: TextView, btn: Button) {
        val v = display.toDoubleOrNull() ?: 0.0
        when (label) {
            "DEG" -> {
                isDeg = !isDeg
                btn.text = if (isDeg) "DEG" else "RAD"
            }
            "sin" -> { display = formatResult(sin(toRad(v))); tv.text = display; newInput = true }
            "cos" -> { display = formatResult(cos(toRad(v))); tv.text = display; newInput = true }
            "tan" -> { display = formatResult(tan(toRad(v))); tv.text = display; newInput = true }
            "ln" -> { display = if (v > 0) formatResult(ln(v)) else "Error"; tv.text = display; newInput = true }
            "log" -> { display = if (v > 0) formatResult(log10(v)) else "Error"; tv.text = display; newInput = true }
            "√" -> { display = if (v >= 0) formatResult(sqrt(v)) else "Error"; tv.text = display; newInput = true }
            "π" -> { display = formatResult(PI); tv.text = display; newInput = false }
            "e" -> { display = formatResult(E); tv.text = display; newInput = false }
            "EXP" -> { display += "E"; tv.text = display }
            "(" -> {
                if (newInput) display = "(" else display += "("
                openParens++; newInput = false; tv.text = display
            }
            ")" -> {
                if (openParens > 0) { display += ")"; openParens--; tv.text = display }
            }
            "^" -> {
                firstNum = v
                expression = "$display ^"
                tvExpr.text = expression
                operator = "^"; newInput = true
            }
            "⌫" -> {
                if (display.length > 1) {
                    display = display.dropLast(1)
                    if (display == "-") display = "0"
                } else {
                    display = "0"
                }
                tv.text = display
            }
            "±" -> { display = formatResult(-v); tv.text = display }
            "%" -> { display = formatResult(v / 100); tv.text = display }
            "=" -> {
                val second = display.toDoubleOrNull() ?: 0.0
                val result = when (operator) {
                    "+" -> firstNum + second
                    "-" -> firstNum - second
                    "×" -> firstNum * second
                    "÷" -> if (second != 0.0) firstNum / second else Double.NaN
                    "^" -> firstNum.pow(second)
                    else -> second
                }
                val firstStr = if (firstNum % 1.0 == 0.0) firstNum.toLong().toString() else firstNum.toString()
                val secondStr = if (second % 1.0 == 0.0) second.toLong().toString() else second.toString()
                HistoryManager.add("$firstStr $operator $secondStr = ${formatResult(result)}")
                tvExpr.text = "$expression $display ="
                display = formatResult(result)
                tv.text = display; newInput = true; expression = ""
            }
            in listOf("+", "-", "×", "÷") -> {
                firstNum = v
                expression = "$display $label"
                tvExpr.text = expression
                operator = label; newInput = true
            }
            else -> {
                if (label.matches(Regex("[0-9.]"))) {
                    if (newInput || display == "0") { display = label; newInput = false }
                    else display += label
                    tv.text = display
                }
            }
        }
    }

    private fun toRad(v: Double) = if (isDeg) Math.toRadians(v) else v

    private fun formatResult(v: Double): String {
        if (v.isNaN()) return "Error"
        if (v.isInfinite()) return "Infinity"
        return if (v % 1.0 == 0.0) v.toLong().toString()
        else "%.8f".format(v).trimEnd('0')
    }
}
package com.yourname.calculatorapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class NormalCalcFragment : Fragment() {
    private var display = "0"
    private var expression = ""
    private var operator = ""
    private var firstNum = 0.0
    private var newInput = true
    private var openParens = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_normal, container, false)
        val tvDisplay = view.findViewById<TextView>(R.id.tvDisplay)
        val tvExpression = view.findViewById<TextView>(R.id.tvExpression)
        val grid = view.findViewById<GridLayout>(R.id.buttonGrid)

        val operators = listOf("÷", "×", "-", "+", "=")
        val labels = listOf("(","±","%","÷","7","8","9","×","4","5","6","-","1","2","3","+","0",".","⌫","=")

        labels.forEach { label ->
            val btn = Button(requireContext()).apply {
                text = label
                textSize = 20f
                val isOperator = label in operators
                setBackgroundColor(android.graphics.Color.parseColor(if (isOperator) "#1A1A1A" else "#111111"))
                setTextColor(android.graphics.Color.WHITE)
                stateListAnimator = null
                val p = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f)
                    setMargins(6, 6, 6, 6)
                }
                layoutParams = p
            }

            btn.setOnClickListener { handleButton(label, tvDisplay, tvExpression) }

            btn.setOnLongClickListener {
                when (label) {
                    "⌫" -> showPopup(btn, "Clear all") {
                        display = "0"; expression = ""
                        tvDisplay.text = "0"; tvExpression.text = ""
                        newInput = true; openParens = 0
                    }
                    "(" -> showPopup(btn, ")") {
                        if (openParens > 0) {
                            display += ")"; openParens--
                            tvDisplay.text = display
                        }
                    }
                    "%" -> showPopup(btn, "√") {
                        val v = display.toDoubleOrNull() ?: 0.0
                        display = if (v >= 0) {
                            val r = Math.sqrt(v)
                            if (r % 1.0 == 0.0) r.toLong().toString()
                            else "%.6f".format(r).trimEnd('0')
                        } else "Error"
                        tvDisplay.text = display
                        newInput = true
                    }
                    "±" -> showPopup(btn, "^") {
                        firstNum = display.toDoubleOrNull() ?: 0.0
                        expression = "$display ^"
                        tvExpression.text = expression
                        operator = "^"
                        newInput = true
                    }
                }
                true
            }

            grid.addView(btn)
        }
        return view
    }

    private fun showPopup(anchor: View, label: String, onClick: () -> Unit) {
        val popup = android.widget.PopupWindow(requireContext())
        val btn = Button(requireContext()).apply {
            text = label
            textSize = 18f
            setBackgroundColor(android.graphics.Color.parseColor("#3A3A3A"))
            setTextColor(android.graphics.Color.WHITE)
            stateListAnimator = null
            gravity = android.view.Gravity.CENTER
        }
        btn.setOnClickListener {
            onClick()
            popup.dismiss()
        }
        popup.contentView = btn
        popup.width = anchor.width
        popup.height = anchor.height
        popup.isOutsideTouchable = true
        popup.isFocusable = true
        popup.showAsDropDown(anchor, 0, -anchor.height * 2)
    }

    private fun handleButton(label: String, tv: TextView, tvExpr: TextView) {
        when {
            label.matches(Regex("[0-9.]")) -> {
                if (newInput || display == "0") { display = label; newInput = false }
                else display += label
                tv.text = display
            }
            label == "(" -> {
                if (newInput) display = "(" else display += "("
                openParens++
                newInput = false
                tv.text = display
            }
            label in listOf("+", "-", "×", "÷") -> {
                firstNum = display.toDoubleOrNull() ?: 0.0
                expression = "$display $label"
                tvExpr.text = expression
                operator = label; newInput = true
            }
            label == "=" -> {
                val second = display.toDoubleOrNull() ?: 0.0
                val result = when (operator) {
                    "+" -> firstNum + second
                    "-" -> firstNum - second
                    "×" -> firstNum * second
                    "÷" -> if (second != 0.0) firstNum / second else Double.NaN
                    "^" -> Math.pow(firstNum, second)
                    else -> second
                }
                val resultStr = if (result % 1.0 == 0.0) result.toLong().toString()
                else "%.6f".format(result).trimEnd('0')
                val firstStr = if (firstNum % 1.0 == 0.0) firstNum.toLong().toString() else firstNum.toString()
                val secondStr = if (second % 1.0 == 0.0) second.toLong().toString() else second.toString()
                HistoryManager.add("$firstStr $operator $secondStr = $resultStr")
                tvExpr.text = "$expression $display ="
                display = resultStr
                tv.text = display
                newInput = true
                expression = ""
                openParens = 0
            }
            label == "⌫" -> {
                if (display.length > 1) {
                    display = display.dropLast(1)
                    if (display == "-") display = "0"
                } else {
                    display = "0"
                }
                tv.text = display
            }
            label == "%" -> {
                val v = display.toDoubleOrNull() ?: 0.0
                display = (v / 100).toString(); tv.text = display
            }
            label == "±" -> {
                val v = display.toDoubleOrNull() ?: 0.0
                display = (-v).toString(); tv.text = display
            }
        }
    }
}
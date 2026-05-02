package com.yourname.calculatorapp
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.yourname.calculatorapp.R
import kotlin.math.abs

class FractionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fraction, container, false)

        val spinner = view.findViewById<Spinner>(R.id.spinnerOp)
        val ops = listOf("+", "−", "×", "÷")
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, ops)

        view.findViewById<Button>(R.id.btnCalcFraction).setOnClickListener {
            val n1 = view.findViewById<EditText>(R.id.etNum1).text.toString().toDoubleOrNull() ?: return@setOnClickListener
            val d1 = view.findViewById<EditText>(R.id.etDen1).text.toString().toDoubleOrNull() ?: return@setOnClickListener
            val n2 = view.findViewById<EditText>(R.id.etNum2).text.toString().toDoubleOrNull() ?: return@setOnClickListener
            val d2 = view.findViewById<EditText>(R.id.etDen2).text.toString().toDoubleOrNull() ?: return@setOnClickListener

            if (d1 == 0.0 || d2 == 0.0) {
                view.findViewById<TextView>(R.id.tvFractionResult).text = "Error: denominator can't be 0"
                return@setOnClickListener
            }

            val (rn, rd) = when (spinner.selectedItem) {
                "+" -> Pair(n1*d2 + n2*d1, d1*d2)
                "−" -> Pair(n1*d2 - n2*d1, d1*d2)
                "×" -> Pair(n1*n2, d1*d2)
                "÷" -> Pair(n1*d2, d1*n2)
                else -> Pair(n1, d1)
            }
            val g = gcd(abs(rn).toLong(), abs(rd).toLong()).toDouble()
            val simplN = (rn/g).toLong()
            val simplD = (rd/g).toLong()
            val decimal = rn/rd

            view.findViewById<TextView>(R.id.tvFractionResult).text =
                "$simplN/$simplD  =  %.4f".format(decimal).trimEnd('0').trimEnd('.')
        }
        return view
    }

    private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
}
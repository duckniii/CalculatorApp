package com.yourname.calculatorapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class TipFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tip, container, false)

        val etTipPercent = view.findViewById<EditText>(R.id.etTipPercent)

        view.findViewById<Button>(R.id.btn10).setOnClickListener { etTipPercent.setText("10") }
        view.findViewById<Button>(R.id.btn15).setOnClickListener { etTipPercent.setText("15") }
        view.findViewById<Button>(R.id.btn20).setOnClickListener { etTipPercent.setText("20") }
        view.findViewById<Button>(R.id.btn25).setOnClickListener { etTipPercent.setText("25") }

        view.findViewById<Button>(R.id.btnCalculateTip).setOnClickListener {
            val bill = view.findViewById<EditText>(R.id.etBillAmount).text.toString().toDoubleOrNull()
            val tip = etTipPercent.text.toString().toDoubleOrNull() ?: 0.0
            val people = view.findViewById<EditText>(R.id.etPeople).text.toString().toIntOrNull() ?: 1

            if (bill == null) {
                Toast.makeText(context, "Enter a bill amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (people < 1) {
                Toast.makeText(context, "Number of people must be at least 1", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipAmount = bill * tip / 100
            val totalBill = bill + tipAmount
            val perPerson = totalBill / people

            view.findViewById<TextView>(R.id.tvTipAmount).text = "%.2f".format(tipAmount)
            view.findViewById<TextView>(R.id.tvTotalBill).text = "%.2f".format(totalBill)
            view.findViewById<TextView>(R.id.tvPerPerson).text = "%.2f".format(perPerson)
        }

        return view
    }
}
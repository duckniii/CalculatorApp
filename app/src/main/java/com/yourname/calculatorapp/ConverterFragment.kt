package com.yourname.calculatorapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class ConverterFragment : Fragment() {

    private var converterType = "Length"

    companion object {
        fun newInstance(type: String): ConverterFragment {
            val fragment = ConverterFragment()
            fragment.converterType = type
            return fragment
        }
    }

    private val units = mapOf(
        "Length" to listOf(
            "Kilometer" to 1000.0,
            "Meter" to 1.0,
            "Centimeter" to 0.01,
            "Millimeter" to 0.001,
            "Mile" to 1609.344,
            "Yard" to 0.9144,
            "Foot" to 0.3048,
            "Inch" to 0.0254
        ),
        "Weight" to listOf(
            "Kilogram" to 1.0,
            "Gram" to 0.001,
            "Milligram" to 0.000001,
            "Pound" to 0.453592,
            "Ounce" to 0.0283495,
            "Ton" to 1000.0
        ),
        "Temperature" to listOf(
            "Celsius" to 1.0,
            "Fahrenheit" to 1.0,
            "Kelvin" to 1.0
        ),
        "Speed" to listOf(
            "km/h" to 1.0,
            "m/s" to 3.6,
            "mph" to 1.60934,
            "knot" to 1.852,
            "ft/s" to 1.09728
        ),
        "Area" to listOf(
            "Square Meter" to 1.0,
            "Square Kilometer" to 1000000.0,
            "Square Foot" to 0.092903,
            "Square Inch" to 0.00064516,
            "Acre" to 4046.86,
            "Hectare" to 10000.0
        ),
        "Volume" to listOf(
            "Liter" to 1.0,
            "Milliliter" to 0.001,
            "Cubic Meter" to 1000.0,
            "Gallon (US)" to 3.78541,
            "Fluid Ounce" to 0.0295735,
            "Cup" to 0.24,
            "Pint" to 0.473176
        ),
        "Pressure" to listOf(
            "Pascal" to 1.0,
            "Kilopascal" to 1000.0,
            "Bar" to 100000.0,
            "PSI" to 6894.76,
            "Atmosphere" to 101325.0,
            "mmHg" to 133.322
        ),
        "Energy" to listOf(
            "Joule" to 1.0,
            "Kilojoule" to 1000.0,
            "Calorie" to 4.184,
            "Kilocalorie" to 4184.0,
            "kWh" to 3600000.0,
            "Watt-hour" to 3600.0,
            "BTU" to 1055.06
        ),
        "Data" to listOf(
            "Bit" to 1.0,
            "Byte" to 8.0,
            "Kilobyte" to 8000.0,
            "Megabyte" to 8000000.0,
            "Gigabyte" to 8000000000.0,
            "Terabyte" to 8000000000000.0,
            "Petabyte" to 8000000000000000.0
        )
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_converter, container, false)

        val unitList = units[converterType] ?: return view
        val unitNames = unitList.map { it.first }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, unitNames)

        view.findViewById<TextView>(R.id.tvConverterTitle).text = "$converterType Converter"
        view.findViewById<Spinner>(R.id.spinnerFrom).adapter = adapter
        view.findViewById<Spinner>(R.id.spinnerTo).adapter = adapter
        view.findViewById<Spinner>(R.id.spinnerTo).setSelection(1)

        view.findViewById<Button>(R.id.btnConvert).setOnClickListener {
            val input = view.findViewById<EditText>(R.id.etConverterInput).text.toString().toDoubleOrNull()
            if (input == null) {
                Toast.makeText(context, "Enter a valid number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fromUnit = view.findViewById<Spinner>(R.id.spinnerFrom).selectedItem.toString()
            val toUnit = view.findViewById<Spinner>(R.id.spinnerTo).selectedItem.toString()

            val result = if (converterType == "Temperature") {
                convertTemperature(input, fromUnit, toUnit)
            } else {
                val fromFactor = unitList.find { it.first == fromUnit }?.second ?: 1.0
                val toFactor = unitList.find { it.first == toUnit }?.second ?: 1.0
                input * fromFactor / toFactor
            }

            val formatted = if (result % 1.0 == 0.0) result.toLong().toString()
            else "%.6f".format(result).trimEnd('0')
            view.findViewById<TextView>(R.id.tvConverterResult).text = "$formatted $toUnit"
        }

        return view
    }

    private fun convertTemperature(value: Double, from: String, to: String): Double {
        val celsius = when (from) {
            "Celsius" -> value
            "Fahrenheit" -> (value - 32) * 5 / 9
            "Kelvin" -> value - 273.15
            else -> value
        }
        return when (to) {
            "Celsius" -> celsius
            "Fahrenheit" -> celsius * 9 / 5 + 32
            "Kelvin" -> celsius + 273.15
            else -> celsius
        }
    }
}
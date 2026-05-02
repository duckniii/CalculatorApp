package com.yourname.calculatorapp
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.yourname.calculatorapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class CurrencyFragment : Fragment() {
    // Uses the free exchangerate-api.com (no key required for basic endpoint)
    private val currencies = listOf("USD","EUR","GBP","HUF","JPY","CHF","CAD","AUD","PLN","CZK","RON","RSD")
    private var rates = mapOf<String,Double>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_currency, container, false)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, currencies)
        view.findViewById<Spinner>(R.id.spinnerFrom).adapter = adapter
        view.findViewById<Spinner>(R.id.spinnerTo).adapter = adapter
        view.findViewById<Spinner>(R.id.spinnerTo).setSelection(1) // Default to EUR

        // Fetch rates on load
        fetchRates(view)

        view.findViewById<Button>(R.id.btnConvert).setOnClickListener {
            val amount = view.findViewById<EditText>(R.id.etAmount).text.toString().toDoubleOrNull()
            if (amount == null) { Toast.makeText(context, "Enter a valid amount", Toast.LENGTH_SHORT).show(); return@setOnClickListener }

            val from = view.findViewById<Spinner>(R.id.spinnerFrom).selectedItem.toString()
            val to   = view.findViewById<Spinner>(R.id.spinnerTo).selectedItem.toString()

            if (rates.isEmpty()) { fetchRates(view); return@setOnClickListener }

            val rateFrom = rates[from] ?: 1.0
            val rateTo   = rates[to] ?: 1.0
            val result   = amount / rateFrom * rateTo

            view.findViewById<TextView>(R.id.tvCurrencyResult).text =
                "%.2f %s".format(result, to)
            view.findViewById<TextView>(R.id.tvRateInfo).text =
                "1 $from = %.4f $to (live rate)".format(rateTo/rateFrom)
        }
        return view
    }

    private fun fetchRates(view: View) {
        view.findViewById<TextView>(R.id.tvRateInfo).text = "Fetching live rates..."
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val json = URL("https://open.er-api.com/v6/latest/USD").readText()
                val obj  = JSONObject(json).getJSONObject("rates")
                rates = currencies.associateWith { obj.optDouble(it, 1.0) }
                withContext(Dispatchers.Main) {
                    view.findViewById<TextView>(R.id.tvRateInfo).text = "Rates loaded successfully"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.findViewById<TextView>(R.id.tvRateInfo).text = "Offline — check internet connection"
                }
            }
        }
    }
}
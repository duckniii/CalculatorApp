package com.yourname.calculatorapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class HistoryFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        val listView = view.findViewById<ListView>(R.id.listHistory)

        val historyList = HistoryManager.getAll().toMutableList()

        val adapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, historyList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val tv = super.getView(position, convertView, parent) as TextView
                tv.setTextColor(android.graphics.Color.WHITE)
                tv.textSize = 16f
                tv.setPadding(8, 16, 8, 16)
                tv.setBackgroundColor(android.graphics.Color.parseColor("#000000"))
                return tv
            }
        }
        listView.adapter = adapter

        if (historyList.isEmpty()) {
            val empty = TextView(requireContext()).apply {
                text = "No history yet"
                textSize = 16f
                setTextColor(android.graphics.Color.parseColor("#888888"))
                gravity = android.view.Gravity.CENTER
                setPadding(0, 64, 0, 0)
            }
            listView.emptyView = empty
            (view as ViewGroup).addView(empty)
        }

        view.findViewById<Button>(R.id.btnClearHistory).setOnClickListener {
            HistoryManager.clear()
            historyList.clear()
            adapter.notifyDataSetChanged()
        }

        return view
    }
}
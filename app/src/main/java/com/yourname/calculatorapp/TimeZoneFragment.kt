package com.yourname.calculatorapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TimeZoneFragment : Fragment() {

    private val timeZones = listOf(
        "Europe/Budapest" to "Budapest (HU)",
        "Europe/Belgrade" to "Belgrade (RS)",
        "Europe/London" to "London (UK)",
        "Europe/Paris" to "Paris (FR)",
        "Europe/Berlin" to "Berlin (DE)",
        "Europe/Rome" to "Rome (IT)",
        "Europe/Madrid" to "Madrid (ES)",
        "Europe/Amsterdam" to "Amsterdam (NL)",
        "Europe/Warsaw" to "Warsaw (PL)",
        "Europe/Prague" to "Prague (CZ)",
        "Europe/Bucharest" to "Bucharest (RO)",
        "Europe/Athens" to "Athens (GR)",
        "Europe/Stockholm" to "Stockholm (SE)",
        "Europe/Vienna" to "Vienna (AT)",
        "Europe/Zurich" to "Zurich (CH)",
        "Europe/Moscow" to "Moscow (RU)",
        "America/New_York" to "New York (US)",
        "America/Chicago" to "Chicago (US)",
        "America/Denver" to "Denver (US)",
        "America/Los_Angeles" to "Los Angeles (US)",
        "America/Toronto" to "Toronto (CA)",
        "America/Sao_Paulo" to "São Paulo (BR)",
        "Asia/Dubai" to "Dubai (AE)",
        "Asia/Istanbul" to "Istanbul (TR)",
        "Asia/Tokyo" to "Tokyo (JP)",
        "Asia/Shanghai" to "Shanghai (CN)",
        "Asia/Singapore" to "Singapore (SG)",
        "Asia/Kolkata" to "Mumbai (IN)",
        "Asia/Seoul" to "Seoul (KR)",
        "Australia/Sydney" to "Sydney (AU)",
        "Africa/Cairo" to "Cairo (EG)",
        "Pacific/Auckland" to "Auckland (NZ)"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_timezone, container, false)

        val names = timeZones.map { it.second }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, names)

        val spinnerFrom = view.findViewById<Spinner>(R.id.spinnerFromZone)
        val spinnerTo = view.findViewById<Spinner>(R.id.spinnerToZone)

        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        // Default: Budapest -> Belgrade (relevant for you!)
        spinnerFrom.setSelection(0)
        spinnerTo.setSelection(1)

        view.findViewById<Button>(R.id.btnConvertTime).setOnClickListener {
            val timeInput = view.findViewById<EditText>(R.id.etTime).text.toString()

            if (timeInput.isEmpty() || !timeInput.contains(":")) {
                Toast.makeText(context, "Enter time in HH:MM format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val parts = timeInput.split(":")
                val hour = parts[0].toInt()
                val minute = parts[1].toInt()

                if (hour !in 0..23 || minute !in 0..59) {
                    Toast.makeText(context, "Invalid time", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val fromZoneId = ZoneId.of(timeZones[spinnerFrom.selectedItemPosition].first)
                val toZoneId = ZoneId.of(timeZones[spinnerTo.selectedItemPosition].first)

                val fromTime = ZonedDateTime.of(LocalDate.now(), LocalTime.of(hour, minute), fromZoneId)
                val toTime = fromTime.withZoneSameInstant(toZoneId)

                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd")

                view.findViewById<TextView>(R.id.tvTimeResult).text = toTime.format(formatter)
                val diffMinutes = toTime.offset.totalSeconds / 60 - fromTime.offset.totalSeconds / 60
                val diffHours = diffMinutes / 60
                val diffMins = Math.abs(diffMinutes % 60)

                val diffText = when {
                    diffMinutes == 0 -> "Same time zone"
                    diffMins == 0 -> if (diffHours > 0) "+${diffHours}h ahead" else "${diffHours}h behind"
                    else -> if (diffMinutes > 0) "+${diffHours}h ${diffMins}min ahead" else "${diffHours}h ${diffMins}min behind"
                }

                view.findViewById<TextView>(R.id.tvTimeInfo).text =
                    "${timeInput} ${timeZones[spinnerFrom.selectedItemPosition].second}\n" +
                            "= ${toTime.format(formatter)} ${timeZones[spinnerTo.selectedItemPosition].second}\n" +
                            "Date: ${toTime.format(dateFormatter)}\n" +
                            "Difference: $diffText"

            } catch (e: Exception) {
                Toast.makeText(context, "Invalid time format", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
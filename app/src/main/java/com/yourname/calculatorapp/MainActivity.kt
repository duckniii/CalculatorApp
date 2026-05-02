package com.yourname.calculatorapp

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import com.yourname.calculatorapp.R

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.fragmentContainer, NormalCalcFragment())
            .commit()

        navView.setNavigationItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_calculator -> NormalCalcFragment()
                R.id.nav_fractions -> FractionFragment()
                R.id.nav_currency -> CurrencyFragment()
                R.id.nav_length -> ConverterFragment.newInstance("Length")
                R.id.nav_weight -> ConverterFragment.newInstance("Weight")
                R.id.nav_temperature -> ConverterFragment.newInstance("Temperature")
                R.id.nav_speed -> ConverterFragment.newInstance("Speed")
                R.id.nav_area -> ConverterFragment.newInstance("Area")
                R.id.nav_volume -> ConverterFragment.newInstance("Volume")
                R.id.nav_scientific -> ScientificFragment()
                R.id.nav_history -> HistoryFragment()
                R.id.nav_tip -> TipFragment()
                R.id.nav_timezone -> TimeZoneFragment()
                R.id.nav_pressure -> ConverterFragment.newInstance("Pressure")
                R.id.nav_energy -> ConverterFragment.newInstance("Energy")
                R.id.nav_data -> ConverterFragment.newInstance("Data")
                else -> NormalCalcFragment()
            }
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            drawerLayout.closeDrawers()
            true
        }
    }
}
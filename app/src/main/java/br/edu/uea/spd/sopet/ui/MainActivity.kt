package br.edu.uea.spd.sopet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.ui.fragment.FavoritesFragment
import br.edu.uea.spd.sopet.ui.fragment.PetListFragment
import br.edu.uea.spd.sopet.ui.fragment.PlacesFragment1
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val petListFragment = PetListFragment()
    private val mapsFragment = PlacesFragment1()
    private val favoritesFrament = FavoritesFragment()

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        replaceFragment(petListFragment)

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.page_1 -> replaceFragment(petListFragment)
                R.id.page_2 -> replaceFragment(mapsFragment)
                R.id.page_3-> replaceFragment(favoritesFrament)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        if(fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_container, fragment)
            transaction.commit()
        }
    }
}
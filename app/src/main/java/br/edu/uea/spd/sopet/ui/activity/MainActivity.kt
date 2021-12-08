package br.edu.uea.spd.sopet.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.ui.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val notificationFragment = NotificationFragment()
    private val mapsFragment = PlacesFragment()
    private val publishFragment = PublishFragment()
    private val profileFragment = ProfileFragment()
    private val homeFragment = FeedHomeFragment()

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        replaceFragment(homeFragment)

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.page_1 -> replaceFragment(homeFragment)
                R.id.page_2 -> replaceFragment(mapsFragment)
                R.id.page_3 -> replaceFragment(publishFragment)
                R.id.page_4 -> replaceFragment(notificationFragment)
                R.id.page_5-> replaceFragment(profileFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }
}
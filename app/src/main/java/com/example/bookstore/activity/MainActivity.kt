package com.example.bookstore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookstore.*
import com.example.bookstore.fragment.AboutAppFragment
import com.example.bookstore.fragment.DashboardFragment
import com.example.bookstore.fragment.FavouritesFragment
import com.example.bookstore.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var coordinatorLayout: CoordinatorLayout

    private var previousItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        openDashBoard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousItem != null)
                previousItem?.isChecked = false

            it.isCheckable = true
            it.isChecked = true
            previousItem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    openDashBoard()
                    drawerLayout.closeDrawers()
                }

                R.id.favourite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouritesFragment())
                        .commit()

                    supportActionBar?.title = "Favourites"

                    drawerLayout.closeDrawers()
                }

                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())
                        .commit()

                    supportActionBar?.title = "Profile"

                    drawerLayout.closeDrawers()
                }

                R.id.about_phone -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutAppFragment())
                        .commit()

                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home)
            drawerLayout.openDrawer(GravityCompat.START)

        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        navigationView = findViewById(R.id.navigation_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        coordinatorLayout = findViewById(R.id.coordinator_layout)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "ToolBar Tittle"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onBackPressed() {
        when(supportFragmentManager.findFragmentById(R.id.frame))
        {
            !is DashboardFragment -> openDashBoard()

            else -> super.onBackPressed()
        }

    }
    private fun openDashBoard() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, DashboardFragment())
            .commit()

        supportActionBar?.title = "DashBoard"

        navigationView.setCheckedItem(R.id.dashboard)
    }

}
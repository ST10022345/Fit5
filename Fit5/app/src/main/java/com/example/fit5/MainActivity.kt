package com.example.fit5

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.fit5.databinding.ActivityMainBinding
import com.example.fit5.ui.About.AboutFragment
import com.example.fit5.ui.exerciseViews.BackExerciseView

import com.example.fit5.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery,R.id.nav_about,R.id.nav_CustomWorkout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //use the nav view to navigate between screens
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.nav_about -> {
                    // Navigate to AboutFragment
                    val fragment = AboutFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, fragment)
                        .addToBackStack(null)
                        .commit()
                    true
                }
             R.id.nav_home->{
                 //  navigate to  HomeFragment
                 val fragment = HomeFragment()
                 supportFragmentManager.beginTransaction()
                     .replace(R.id.nav_host_fragment_content_main, fragment)
                     .addToBackStack(null)
                     .commit()
                 true
             }
                R.id.nav_gallery->{
                    val intent = Intent(this, ProgressPictureLibrary::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_CustomWorkout->{
                    val intent = Intent(this, CustomRoutine::class.java)
                    startActivity(intent)
                    true
                }




                else -> false
            }

        }





    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
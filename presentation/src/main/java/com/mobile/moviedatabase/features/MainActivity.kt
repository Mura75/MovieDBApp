package com.mobile.moviedatabase.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.mobile.moviedatabase.R
import com.mobile.moviedatabase.features.movies.list.MoviesListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var hostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupActionBarWithNavController(this, hostFragment.navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return hostFragment.navController.navigateUp()
    }
}

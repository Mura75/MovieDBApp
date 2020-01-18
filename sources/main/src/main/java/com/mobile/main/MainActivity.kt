package com.mobile.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.mobile.core_api.mediator.AppWithFacade
import com.mobile.main.di.MainComponent

class MainActivity : AppCompatActivity() {

//    private val hostFragment by lazy {
//        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val appWithFacade = (application as MovieDBApp) as AppWithFacade
        MainComponent.create((application as AppWithFacade).getFacade()).inject(this)
        //NavigationUI.setupActionBarWithNavController(this, hostFragment.navController)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return hostFragment.navController.navigateUp()
//    }
}

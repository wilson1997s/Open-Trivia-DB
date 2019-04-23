package com.example.opentriviadbapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.opentriviadbapp.R.layout.activity_main
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        this.setSupportActionBar(tb_main_act)

        val navController = Navigation.findNavController(this, R.id.fragment)

        setupActionBar(navController)

    }

    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupWithNavController(tb_main_act, navController)
    }


}

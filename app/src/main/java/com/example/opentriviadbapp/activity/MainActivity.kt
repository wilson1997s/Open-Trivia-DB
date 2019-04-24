package com.example.opentriviadbapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.opentriviadbapp.R
import com.example.opentriviadbapp.R.layout.activity_main
import com.example.opentriviadbapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

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

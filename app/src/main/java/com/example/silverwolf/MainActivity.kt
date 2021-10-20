package com.example.silverwolf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.silverwolf.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //iv_note is the name of the imagen view id
        binding.silverWolfLogo.alpha = 0f
        binding.silverWolfLogo.animate().setDuration(2500).alpha(1f).withEndAction{
            startActivity(Intent(this,LoginActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }

        //goToLogin()
    }

    /*private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }*/
}
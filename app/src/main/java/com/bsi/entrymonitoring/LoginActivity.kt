package com.bsi.entrymonitoring

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val logoImageView = findViewById<ImageView>(R.id.logo_bumisuksesindo)

        logoImageView.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }
}
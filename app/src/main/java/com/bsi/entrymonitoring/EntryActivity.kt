package com.bsi.entrymonitoring

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView

class EntryActivity : AppCompatActivity() {
    private var clickCount = 0
    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entry)

        val logoImageView = findViewById<ImageView>(R.id.logo_bumisuksesindo)

        logoImageView.setOnClickListener {
            val currentTime = System.currentTimeMillis()

            if (clickCount == 0) {
                startTime = currentTime
            }

            if (currentTime - startTime <= 1500) {
                clickCount++
                if (clickCount == 5) {
                    clickCount = 0
                    startTime = 0
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            } else {
                clickCount = 1
                startTime = currentTime
            }
        }
    }
}
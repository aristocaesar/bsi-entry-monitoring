package com.bsi.entrymonitoring

import android.content.Context
import android.content.SharedPreferences
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView

class EntryActivity : AppCompatActivity() {
    private var clickCount = 0
    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPreferences : SharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val doorID = sharedPreferences.getString("doorID", "")

        setContentView(R.layout.activity_entry)

        val labelDoorID = findViewById<TextView>(R.id.door_id)
        val logoImageView = findViewById<ImageView>(R.id.logo_bumisuksesindo)

        labelDoorID.text = doorID

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
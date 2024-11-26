package com.bsi.entrymonitoring

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val serverAddress = sharedPreferences.getString("serverAddress", "localhost") ?: "localhost"
        val doorID = sharedPreferences.getString("doorID", "-") ?: "-"
        val username = sharedPreferences.getString("username_mqtt", "")
        val password = sharedPreferences.getString("password_mqtt", "")

        if (!isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_setting)

        val labelDoorID = findViewById<TextView>(R.id.door_id)
        val editTextServerAddress = findViewById<EditText>(R.id.editTextServerAddress)
        val editTextDoorID = findViewById<EditText>(R.id.editTextDoorID)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonSave = findViewById<Button>(R.id.button_save)
        val buttonCancel = findViewById<Button>(R.id.button_cancel)
        val togglePasswordVisibility = findViewById<ImageView>(R.id.togglePasswordVisibility)

        labelDoorID.text = doorID
        editTextServerAddress.setText(serverAddress)
        editTextDoorID.setText(doorID)
        editTextUsername.setText(username)
        editTextPassword.setText(password)

        togglePasswordVisibility.setOnClickListener {
            if (isPasswordVisible) {
                editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordVisibility.setImageResource(R.drawable.ic_eye_off)
            } else {
                editTextPassword.inputType = InputType.TYPE_CLASS_TEXT
                togglePasswordVisibility.setImageResource(R.drawable.ic_eye_on)
            }
            editTextPassword.setSelection(editTextPassword.text.length)
            isPasswordVisible = !isPasswordVisible
        }

        buttonCancel.setOnClickListener {
            with(sharedPreferences.edit()) {
                remove("isLoggedIn")
                remove("username")
                apply()
            }

            startActivity(Intent(this, EntryActivity::class.java))
            finish()
        }

        buttonSave.setOnClickListener {
            with(sharedPreferences.edit()) {
                putString("serverAddress", editTextServerAddress.text.toString())
                putString("doorID", editTextDoorID.text.toString())
                putString("username_mqtt", editTextUsername.text.toString())
                putString("password_mqtt", editTextPassword.text.toString())
                apply()
            }

            val checkServer = true
            if(!checkServer) {
                startActivity(Intent(this, EntryActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Invalid Configuration, Please Check Again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStop() {
        super.onStop()

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("isLoggedIn")
            remove("username")
            apply()
        }
    }
}
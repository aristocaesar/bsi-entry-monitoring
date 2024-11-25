package com.bsi.entrymonitoring

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.button_login)
        val togglePasswordVisibility = findViewById<ImageView>(R.id.togglePasswordVisibility)

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

        buttonLogin.setOnClickListener {
            if(editTextUsername.text.toString() == "admin" && editTextPassword.text.toString() == "12345") {
                startActivity(Intent(this, SettingActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
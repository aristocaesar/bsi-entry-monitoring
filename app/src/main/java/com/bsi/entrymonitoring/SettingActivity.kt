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

class SettingActivity : AppCompatActivity() {
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)

        val editTextServerAddress = findViewById<EditText>(R.id.editTextServerAddress)
        val editTextDoorID = findViewById<EditText>(R.id.editTextDoorID)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonSave = findViewById<Button>(R.id.button_save)
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

        buttonSave.setOnClickListener {
            var checkServer = true;
            if(!checkServer) {
                startActivity(Intent(this, EntryActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Invalid Configuration, Please Check Again!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
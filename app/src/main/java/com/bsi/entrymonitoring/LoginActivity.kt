package com.bsi.entrymonitoring

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.bsi.entrymonitoring.api.RetrofitClient
import com.bsi.entrymonitoring.model.User
import com.bsi.entrymonitoring.utils.AlertDialogManager
import com.bsi.entrymonitoring.utils.NetworkManager
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPreferences : SharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val doorID = sharedPreferences.getString("doorID", "-") ?: "-"

        if (isLoggedIn) {
            startActivity(Intent(this, SettingActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_login)

        val labelDoorID = findViewById<TextView>(R.id.door_id)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.button_login)
        val togglePasswordVisibility = findViewById<ImageView>(R.id.togglePasswordVisibility)

        labelDoorID.text = doorID

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
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString().toMd5()

            if (!NetworkManager.isNetworkAvailable(this)) {
                NetworkManager.showNetworkErrorDialog(this)
            }else {
                RetrofitClient.instance.login(username, password)
                    .enqueue(object : Callback<List<User>> {
                        override fun onResponse(
                            call: Call<List<User>>,
                            response: Response<List<User>>
                        ) {
                            if (response.isSuccessful) {
                                val data = response.body()
                                if (data.isNullOrEmpty()) {
                                    AlertDialogManager.showErrorDialog(
                                        this@LoginActivity, message = "Invalid username or password", positiveButtonText = "OK")
                                } else {
                                    val firstUser = data[0]
                                    with(sharedPreferences.edit()) {
                                        putBoolean("isLoggedIn", true)
                                        putString("username", firstUser.idBadge)
                                        apply()
                                    }

                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            SettingActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                            } else {
                                AlertDialogManager.showErrorDialog(
                                    this@LoginActivity, message = "Error: ${response.code()}", positiveButtonText = "OK")
                            }
                        }

                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            AlertDialogManager.showErrorDialog(
                                this@LoginActivity, message = "Failure: ${t.message}", positiveButtonText = "OK")
                        }
                    })
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

    private fun String.toMd5(): String {
        val md5 = MessageDigest.getInstance("MD5")
        val bytes = md5.digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
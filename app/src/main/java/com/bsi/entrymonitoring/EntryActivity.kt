package com.bsi.entrymonitoring

import com.bsi.entrymonitoring.utils.MqttClientManager
import android.content.Context
import android.content.SharedPreferences
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bsi.entrymonitoring.api.RetrofitClient
import com.bsi.entrymonitoring.model.Employee
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

class EntryActivity : AppCompatActivity() {
    private lateinit var mqttClientManager: MqttClientManager
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

    override fun onStart() {
        super.onStart()

        val sharedPreferences : SharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val serverAddress = sharedPreferences.getString("serverAddress", "") ?: "localhost"
        val doorID = sharedPreferences.getString("doorID", "-") ?: "-"

        val labelDoorID = findViewById<TextView>(R.id.door_id)
        labelDoorID.text = doorID

        val activityTitle = findViewById<TextView>(R.id.activity_title)
        val bodyTitle = findViewById<TextView>(R.id.body_title)
        val iconSignal = findViewById<ImageView>(R.id.icon_signal)

        val payloadContainerImage = findViewById<View>(R.id.result_payload_container_image)
        val payloadImage = findViewById<ImageView>(R.id.result_payload_image)
        val payloadName = findViewById<TextView>(R.id.result_payload_name)
        val payloadCardID = findViewById<TextView>(R.id.result_payload_card_id)
        val payloadDepartment = findViewById<TextView>(R.id.result_payload_department)
        val payloadCompany = findViewById<TextView>(R.id.result_payload_company)

        mqttClientManager = MqttClientManager(serverAddress, "Client123")

        mqttClientManager.connect(
            onConnected = {
                runOnUiThread {
                    Toast.makeText(this, "Connected to MQTT broker", Toast.LENGTH_SHORT).show()
                }

                mqttClientManager.subscribe(doorID,
                    onSubscribed = {
                        runOnUiThread {
                            Toast.makeText(this, "Subscribed to door $doorID", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onFailure = { error ->
                        runOnUiThread {
                            Toast.makeText(this, "Subscribe failed: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            },
            onFailure = { error ->
                runOnUiThread {
                    Toast.makeText(this, "Connection failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )

        mqttClientManager.receivedMessage.observe(this) { message ->
            activityTitle.visibility = View.GONE
            bodyTitle.visibility = View.GONE
            iconSignal.visibility = View.GONE

            payloadContainerImage.visibility = View.VISIBLE
            payloadImage.visibility = View.VISIBLE
            payloadName.visibility = View.VISIBLE
            payloadCardID.visibility = View.VISIBLE
            payloadDepartment.visibility = View.VISIBLE
            payloadCompany.visibility = View.VISIBLE

            if (message.isNotEmpty()) {
                RetrofitClient.instance.getDoor(doorID).enqueue(object : Callback<List<Employee>> {
                    override fun onResponse(call: Call<List<Employee>>, response: Response<List<Employee>>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data.isNullOrEmpty()) {
                                Toast.makeText(this@EntryActivity, "Invalid Employee", Toast.LENGTH_SHORT).show()
                            } else {
                                val result = data[0]
                                payloadName.text = result.name
                                payloadCardID.text = result.idBadge
                                payloadDepartment.text = message
                                payloadCompany.text = result.company

                                if(result.photo.isNotEmpty()) {
                                    val decodedString : ByteArray =  Base64.decode(result.photo, Base64.DEFAULT)
                                    val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                                    payloadImage.setImageBitmap(decodedByte)
                                }
                            }
                        } else {
                            Toast.makeText(this@EntryActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Employee>>, t: Throwable) {
                        Toast.makeText(this@EntryActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mqttClientManager.isConnected()) mqttClientManager.disconnect {}
    }
}
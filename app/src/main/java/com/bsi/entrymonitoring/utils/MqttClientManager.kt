package com.bsi.entrymonitoring.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.eclipse.paho.client.mqttv3.*

class MqttClientManager(private val brokerUrl: String, private val clientId: String) {

    private var mqttClient: MqttClient? = null
    private var mqttConnectOptions: MqttConnectOptions = MqttConnectOptions()
    private val _receivedMessage = MutableLiveData<String>()
    val receivedMessage: LiveData<String> get() = _receivedMessage

    init {
        mqttConnectOptions.isCleanSession = true
    }


    fun connect(onConnected: () -> Unit, onFailure: (Throwable) -> Unit) {
        mqttClient = MqttClient("tcp://$brokerUrl:1883", clientId, null)

        mqttClient?.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {}

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                message?.let {
                    _receivedMessage.postValue(it.toString())
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })

        try {
            mqttClient?.connect(mqttConnectOptions)
            onConnected()
        } catch (e: MqttException) {
            onFailure(e)
        }
    }

    fun subscribe(topic: String, qos: Int = 1, onSubscribed: () -> Unit, onFailure: (Throwable) -> Unit) {
        try {
            mqttClient?.subscribe(topic, qos)
            onSubscribed()
        } catch (e: MqttException) {
            onFailure(e)
        }
    }

    fun isConnected(): Boolean {
        return mqttClient?.isConnected ?: false
    }

    fun disconnect(onDisconnected: () -> Unit) {
        mqttClient?.disconnect()
        onDisconnected()
    }
}

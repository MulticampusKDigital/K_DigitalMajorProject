package com.goni99.major_project_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.goni99.major_project_app.R
import com.goni99.major_project_app.network.MyMqtt
import kotlinx.android.synthetic.main.activity_dryer.*
import org.eclipse.paho.client.mqttv3.MqttMessage

class DryerActivity : AppCompatActivity(), View.OnClickListener {
    val sub_topic = "iot/#"
    val server_uri = "tcp://192.168.45.85:1883"
    var mymqtt: MyMqtt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dryer)

        mymqtt = MyMqtt(this, server_uri)
        mymqtt?.mySetCallback(::onRecieved)
        mymqtt?.connect(arrayOf<String>(sub_topic))
        up.setOnClickListener(this)
        down.setOnClickListener(this)
    }

    fun onRecieved(topic: String, message: MqttMessage) {
        val msg = String(message.payload)
        Log.d("mymqtt", msg)
    }

    override fun onClick(v: View?) {
        var data:String = ""
        if (v?.id == R.id.up) {
            data = "up"
        } else {
            data = "down"
        }
        mymqtt?.publish("android/iot", data)
    }
}
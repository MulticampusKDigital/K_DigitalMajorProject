package com.goni99.major_project_app.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.goni99.major_project_app.R
import com.goni99.major_project_app.databinding.ActivityWashinMachineBinding
import com.goni99.major_project_app.network.MyMqtt
import org.eclipse.paho.client.mqttv3.MqttMessage


class WashingMachineActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityWashinMachineBinding.inflate(layoutInflater)
    }

    val subTopic = "iot/#"
    val serverUri = "tcp://192.168.35.5:1883"
    var myMqtt: MyMqtt? = null

    var height= 0


    private lateinit var notificationManager: NotificationManager

    companion object {
        const val CHANNEL_ID = "test_channel_id"
        const val CHANNEL_NAME = "test_channel_name"
        const val NOTIFICATION_ID = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        myMqtt = MyMqtt(this, serverUri)
        myMqtt?.mySetCallback(::onReceived)
        myMqtt?.connect(arrayOf(subTopic))
        createChannel()



        with(binding){
            level1Button.setOnClickListener {
                waterHeightImageView.setImageResource(R.drawable.full_water)
                waterHeightLevelTextView.text = "1"
                height = 1
                myMqtt?.publish("iot/android/waterHeight", "1")
            }
            level2Button.setOnClickListener {
                waterHeightImageView.setImageResource(R.drawable.full_water)
                waterHeightLevelTextView.text = "2"
                height = 2
                myMqtt?.publish("iot/android/waterHeight", "2")
            }
            level3Button.setOnClickListener {
                waterHeightImageView.setImageResource(R.drawable.full_water)
                waterHeightLevelTextView.text = "3"
                height = 3
                myMqtt?.publish("iot/android/waterHeight", "3")
            }
            level4Button.setOnClickListener {
                waterHeightImageView.setImageResource(R.drawable.full_water)
                waterHeightLevelTextView.text = "4"
                height = 4
                myMqtt?.publish("iot/android/waterHeight", "4")
            }
            level5Button.setOnClickListener {
                waterHeightImageView.setImageResource(R.drawable.full_water)
                waterHeightLevelTextView.text = "5"
                height = 5
                myMqtt?.publish("iot/android/waterHeight", "5")
            }
            level6Button.setOnClickListener {
                waterHeightImageView.setImageResource(R.drawable.full_water)
                waterHeightLevelTextView.text = "6"
                height = 6
                myMqtt?.publish("iot/android/waterHeight", "6")
            }


            startButton.setOnClickListener {
                if (height == 0){
                    Toast.makeText(applicationContext, "물높이를 설정해주세요!", Toast.LENGTH_SHORT).show()
                } else {
                    myMqtt?.publish("iot/android/servo", "washing_machine_servo_start")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.washingMachineLoading.visibility = View.INVISIBLE
    }

    fun onReceived(topic:String, message: MqttMessage) {
        val msg = String(message.payload)
        Log.d("mymqtt", "onReceived topic : $topic, msg : $msg")

        when (msg){
            "servo_end" -> {
                binding.washingMachineLoading.visibility = View.VISIBLE
                binding.loadingTextView.text = "세탁 준비중..."
            }
            "water_on" -> {
                binding.loadingTextView.text = "세탁물 준비중..."
            }
            "water_off" -> {
                binding.loadingTextView.text = "세탁 준비중..."
            }
            "machine_start" -> {
                binding.loadingTextView.text = "세탁중..."
                binding.washingMachineActivateImage.setImageResource(R.drawable.yes_washing_machine)
            }
            "machine_finish" -> {
                binding.loadingTextView.text = "세탁물 빼는중..."
            }
            "all_finish" -> {
                binding.washingMachineLoading.visibility = View.INVISIBLE
                binding.washingMachineActivateImage.setImageResource(R.drawable.no_washing_machine)
                Toast.makeText(this, "세탁 끝!", Toast.LENGTH_SHORT).show()
                loadNotification()
            }

        }

    }

    fun loadNotification(){
        val intent = Intent(this, WashingMachineActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("세탁 완료")
            .setContentText("세탁이 완료되었습니다.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this@WashingMachineActivity)){
            notify(NOTIFICATION_ID, builder.build())
        }

    }

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel Description"
            }
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}
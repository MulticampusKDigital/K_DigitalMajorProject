package com.goni99.major_project_app.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.goni99.major_project_app.R
import com.goni99.major_project_app.network.MyMqtt
import kotlinx.android.synthetic.main.closet.*
import org.eclipse.paho.client.mqttv3.MqttMessage


//화면디자인 - 화면에 있는 위젯들의 이벤트에 반응하는 처리만 구현
class ClosetActivity : AppCompatActivity(), View.OnClickListener {
    val sub_topic = "iot/#"
    val server_uri = "tcp://192.168.35.5:1883" //broker의 ip와 port
    var mymqtt: MyMqtt? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.closet)


        val intent = intent
        //mqtt통신을 수행할 mqtt객체를 생성
        mymqtt = MyMqtt(this, server_uri)
        //브로커에서 메세지 전달되면 호출될 메소드를 넘기기
        mymqtt?.mySetCallback(::onReceived) //바이트코드를 아예 참조할 수 있게 사용 ( :: )
        //브로커연결
        mymqtt?.connect(arrayOf<String>(sub_topic))

        //이벤트 연결
        btn_left.setOnClickListener(this)
        btn_stop.setOnClickListener(this)
        btn_right.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        var data:String = ""
        if(v?.id== R.id.btn_left){
            data = "left"
        }else if(v?.id== R.id.btn_stop){
            data = "stop"
        }else if(v?.id == R.id.btn_right){
            data = "right"
        }
        mymqtt?.publish("iot/step", data)
    }

    fun onReceived(topic: String, message: MqttMessage){
        val builder = NotificationCompat.Builder(this, "1111")
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle("옷장 알림")
            .setContentText("제습제 교체 필요")
            // Android 7.1 이하를 지원하려면 아래에 표시된 대로 setPriority 설정해야 함
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // 알림 클릭 시 사라짐

        //토픽의 수신을 처리 - 물높이 센서의 값에 따라 특정 메시지가 오면 사용자에게 notification을 띄워준다.
        val msg = String(message.payload)
        val msgdata = msg.split(':')
        Log.d("mymqtt", msg + "," + msgdata[3])
        Log.d("mymqtt", "${msgdata[3].trim().length}")
        if(msgdata[3].trim()=="change"){
//            Log.d("mymqtt", "${msgdata[3]}+제대로 왔음") 테스트용 Log

            createChannel(builder, "1111")



        }else if(msgdata[3].trim()=="change_not"){
            Log.d("mymqtt", "${msgdata[3]}+제습제 교체 전")

        }
    }

    fun createChannel(builder: NotificationCompat.Builder, id: String){

        //낮은 버전을 사용하기 위한 사용자가 있는 경우  - 8.0부터 채널을 통해 관리함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //안드로이드 8.0오레오 버전인경우
            Log.d("test", "if")
            val channel =
                NotificationChannel(id, "mychannel", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //알림메니저를 통해 채널을 등록한다. - Builder에 의해 만들어진 Notification은 체널에 의해 관리된다.
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(Integer.parseInt(id), builder.build())
        } else {
            //이전 버전은 옛날방식으로 Builder를 얻어오기
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(Integer.parseInt(id), builder.build())
            Log.d("test", "else")
        }
    }
}
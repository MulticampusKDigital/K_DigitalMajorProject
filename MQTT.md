# MQTT
<img src="https://jgtonys.github.io/public/img/mqtt/intro.png">
MQTT는 Message Queuing Telemetry Transport의 약자이다. 많은 IOT 기기들을 제어하기위해서 MQTT라는 서버를 통해 통신되어진다.

 MQTT는 브로커(서버), SubScriber, Publish 세가지로 나뉜다.

# Broker
MQTT 에서의 Broker는 서버를 말할 수 있다.
```bash
// path에 mosquitto 경로 지정
mosquitto -v 

// webSocket과 함께 사용할 때
// 경로는 ProgramFiles/mosquitto
mosquitto -c moquitto.conf -v
```

# Subscriber
서브는 항상 구독 할 토픽명을 가지고 있어야 한다.<br>
-t "iot" 와 같이 iot라는 토픽명을 가진 토픽을 구독하고 있다는 뜻이다.<br>
만약 -t "iot/#'와 같이 # 을 입력하게 되면 iot/a, iot/b... 다양하게 뒤에 아무거나 붙어도 구독이 가능하다.
## cmd로 편하게 sub하기
```bash
mosquitto_sub -t "iot"
```

# Publish
퍼블리시는 구독자가 구독한 사람에게 메시지를 보내는 것이다. 그리고 메시지를 받은 서브에서 반응을 하여 퍼블리시가 서브가 될 수도 있다.
## cmd로 편하게 sub하기
```bash
mosquitto_pub -t "iot" -m "test hi~"

mosquitto_pub -h 192.123.45.6(broker의 ip) -p 1883 -t iot -m "hello"

// 여러 개의 값 전달
mosquitto_pub -h 192.123.45.6(broker의 ip) -p 1883 -t iot -m "{\"h\":65, \"t\":24}"
```


# Reference
[#1 MQTT란 무엇인가?]<br>
https://jgtonys.github.io/iot/2018/07/13/mqtt-test/


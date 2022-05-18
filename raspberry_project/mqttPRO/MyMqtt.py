import paho.mqtt.client as mqtt
import threading
from Sensor.DHT11 import DHT11
from Sensor.DC import DC
from Sensor.Servo import Servo


class MyMqtt:
  def __init__(self):
    self.client = mqtt.Client()
    self.client.on_connect = self.on_connect
    self.client.on_message = self.on_message
    # self.dht11 = DHT11(self.client)
    self.dc = None
    self.servo = None
    self.load_sensor()
    
  def load_sensor(self):
    pass
    # self.dht11.start()
    # self.dc.start()
    
  def mymqtt_connect(self):
    try:
      print("서버 연결중...")
      self.client.connect("192.168.35.5", 1883, 60)
      myThread = threading.Thread(target=self.client.loop_forever)
      myThread.start()
    except KeyboardInterrupt:
      pass
    finally:
      print("mymqtt_connect exit")
      
  def on_connect(self, client, userdata, flags, rc):
    print("MyMqtt connecting..." + str(rc))
    if rc == 0:
      client.subscribe("iot/#")
    else:
      print("Failed connect...")
      
  def on_message(self, client, userdata, message):
    msg = message.payload.decode("utf-8")
          
          
    if message.topic == "iot/android/servo":
      if msg == "washing_machine_servo_start":
        self.servo = Servo(self.client)
        self.servo.start()
        print("=====================안드로이드에서 세탁기 시작이다!!====")
        print("서보 움직이기(세탁할 옷 넣기)")
        print("=====================")
        self.client.publish("iot/android", "servo_end")
    if message.topic == "iot/arduino/water":
      if msg == "end":
        print("=====================물 다 넣었따 dc모터 시작====")
        print("세탁기 돌아가는중...")
        print("=====================")
        self.client.publish("iot/android", "machine_start")
        self.dc = DC(self.client)
        self.dc.start()
    
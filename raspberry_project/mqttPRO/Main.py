from bluetooth import *
import threading
import RPi.GPIO as gpio
import paho.mqtt.client as mqtt
import time
from Sensor.receive_HC06 import ReceiveHC06
from Sensor.send_HC06 import SendHC06

h = 0

class MyDevice:
  def __init__(self):
    self.client = mqtt.Client()
    self.client.on_connect = self.on_connect
    self.client.on_message = self.on_message
    self.socket = BluetoothSocket( RFCOMM )
    self.connect_bluetooth()
    self.sendHC06 = SendHC06(self.client, self.socket, h)
    self.receiveHC06 = ReceiveHC06(self.client, self.socket)
    self.receiveHC06.start()
  
  def connect_bluetooth(self):
    self.socket.connect(("98:DA:20:03:82:47", 1))
    print("bluetooth connected!")
  
  def mydevice_connect(self):
    try:
      print("서버 연결중...")
      self.client.connect("192.168.35.5", 1883, 60)
      myThread = threading.Thread(target=self.client.loop_forever)
      myThread.start()
    except KeyboardInterrupt:
      pass
    finally:
      print("mydevice_connect exit")
  
  def on_connect(self, client, userdata, flags, rc):
    print("MyDevice connecting..." + str(rc))
    if rc == 0:
      client.subscribe("iot/#")
    else: print("MyDevice connect failed")
    
  def on_message(self, client, userdata, message):
    global h
    msg = message.payload.decode("utf-8")
    if message.topic == "iot/android/waterHeight":
      h = int(msg)
      print("=====앱 -> 물높이 도착====")
      print(h)
      print("=========================")
    if message.topic == "iot/raspberry/servo":
      if msg == "servo_end":
        print(msg)
        self.sendHC06 = SendHC06(self.client, self.socket, h)
        self.sendHC06.start()
    if message.topic == "iot/raspberry/dc":
      if msg == "dc_motor_finish":
        self.socket.send(msg)
    
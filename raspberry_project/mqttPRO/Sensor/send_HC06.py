from bluetooth import *
import threading
import time

class SendHC06(threading.Thread):
  def __init__(self, client, socket, h):
    super().__init__()
    self.height = h
    self.client = client
    self.socket = socket
    
  def send_message(self,msg):
    self.socket.send(msg)
  
  # 1~6 단계 물높이
      # 1 : 1.25 sec
      # 2 : 2.5 sec
      # 3 : 3.75 sec
      # 4 : 5 sec
      # 5 : 6.25 sec
      # 6 : 7.5 sec
  def control_water_height(self, h):
    print("=========== 물높이 잘왔나?====")
    print("물 높이 : ", h)
    print("물 속도 : ", 1.25*h)
    print("===============")
    self.send_message("on")
    self.client.publish("iot/android", "water_on")
    time.sleep(1.25 * h)
    self.send_message("off")
    self.client.publish("iot/android", "water_off")
    
  def run(self):
    while True:
      try:
        self.control_water_height(self.height)
        time.sleep(2)
        self.client.publish("iot/arduino/water", "end")
        break
      except RuntimeError as error:
        print(error.args[0])
      finally:
        print("send_HC06 finished")
      
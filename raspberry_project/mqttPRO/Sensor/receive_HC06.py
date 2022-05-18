from bluetooth import *
import threading
import time

class ReceiveHC06(threading.Thread):
  def __init__(self, client, socket):
    super().__init__()
    self.client = client
    self.socket = socket
    
  def receive_message(self):
    data = self.socket.recv(1024)
    result = data.decode('utf-8')
    if result == "all_finish":
      self.client.publish("iot/android", "all_finish")
    
    # for d in data:
    #   print(d)
            # if d in (0,1,2,3,4,5,6,7,8,9,10):
            #     print("값 : ", d)
            #     if d == 8:
            #         self.socket.send("dc motor on")
    
  
  def run(self):
    while True:
      try:
        while True:
          self.receive_message()
      except RuntimeError as error:
        print(error.args[0])
      finally:
        print("receive_HC06 finished")
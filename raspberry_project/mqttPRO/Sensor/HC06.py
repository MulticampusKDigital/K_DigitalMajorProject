from bluetooth import *
import threading

class HC06(threading.Thread):
  def __init__(self, client):
    super().__init__()
    self.client = client
    self.socket = BluetoothSocket( RFCOMM )
    self.connect_bluetooth()
  
  def receive_message(self):
    data = self.socket.recv(1024)
    for d in data:
            if d in (0,1,2,3,4,5,6,7,8,9,10):
                print("값 : ", d)
                if d == 8:
                    self.socket.send("dc motor on")
    
  def send_message(self,msg):
    self.socket.send(msg)
  
  def connect_bluetooth(self):
    self.socket.connect(("98:DA:20:03:82:47", 1))
    print("bluetooth connected!")
  
  def run(self):
    while True:
      try:
        while True:
          self.send_message("아두이노로 보낼 데이터 입력")
          self.receive_message()
      except RuntimeError as error:
        print(error.args[0])
      finally:
        print("finished")
        self.socket.close()
      
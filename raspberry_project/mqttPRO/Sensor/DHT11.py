import threading
import time
import paho.mqtt.publish as publisher
import board
import adafruit_dht

class DHT11(threading.Thread):
  def __init__(self, client):
    super().__init__()
    self.mydht11 = adafruit_dht.DHT11(board.D26)
    self.client = client
  
  def run(self):
    while True:
      try:
        while True:
          humidityData = self.mydht11.humidity
          temperatureData = self.mydht11.temperature
          print("습도 : {}%\t 온도 : {}°C".format(humidityData, temperatureData))
          data = str(humidityData) + ":" + str(temperatureData)
          self.client.publish("iot/dht11", data)
          # publisher.single("iot/dht11", "qqqq/", "192.168.35.137")
          # publisher.single("iot/dht11", temperatureData, "192.168.35.137")
          time.sleep(2)
      except RuntimeError as error:
        print(error.args[0])
      finally:
        pass
  
  
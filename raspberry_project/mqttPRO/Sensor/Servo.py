import threading
import time
import RPi.GPIO as gpio

from Sensor.send_HC06 import SendHC06

servo_pin = 21
gpio.setmode(gpio.BCM)
gpio.setup(servo_pin, gpio.OUT)
pwm = gpio.PWM(servo_pin, 50)

class Servo(threading.Thread):
  def __init__(self, client):
    super().__init__()
    self.client = client
    pwm.start(self.getDutyCycle(90))
    time.sleep(0.3)
    gpio.setup(servo_pin, gpio.IN)
    
  def getDutyCycle(self, degree):
    return 2.5 + degree * (1/18)
  
  def setServo(self, i):
    gpio.setup(servo_pin, gpio.OUT)
    pwm.ChangeDutyCycle(self.getDutyCycle(i))
    time.sleep(0.3)
    gpio.setup(servo_pin, gpio.IN)
    
  
  def run(self):
    while True:
      try:
        self.setServo(0)
        time.sleep(2.7)
        self.setServo(90)
        time.sleep(2.7)
        self.client.publish("iot/raspberry/servo", "servo_end")
        break
      except RuntimeError as error:
        print(error.args[0])
      finally:
        pass
      
      
    
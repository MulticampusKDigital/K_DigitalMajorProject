import threading
import time
import RPi.GPIO as gpio

gpio.setmode(gpio.BCM)

STOP = 0
FORWARD = 1
BACKWARD = 2
CH1 = 0
ENA = 26
IN1 = 19
IN2 = 13

gpio.setup(ENA, gpio.OUT)
gpio.setup(IN1, gpio.OUT)
gpio.setup(IN2, gpio.OUT)


pwm = gpio.PWM(ENA, 100)


class DC(threading.Thread):
  def __init__(self, client):
    super().__init__()
    self.client = client
    pwm.start(0)
    
  # 모터 제어 함수
  def setMotorControl(self, pwm, INA, INB, speed, state):
    # 모터 속도 제어 pwm
    pwm.ChangeDutyCycle(speed)
    
    # 앞으로
    if state == FORWARD:
      gpio.output(INA, gpio.HIGH)
      gpio.output(INB, gpio.LOW)
    # 뒤로
    elif state == BACKWARD:
      gpio.output(INA, gpio.LOW)
      gpio.output(INB, gpio.HIGH)
    # 정지
    elif state == STOP:
      gpio.output(INA, gpio.LOW)
      gpio.output(INB, gpio.LOW)
  
  # 모터 제어함수 감단하게 사용하기 위해서 한번더 래핑
  def setMotor(self, ch, speed, state):
    if ch == CH1:
      # pwmA는 핀 설정 후 pwm 핸들을 리턴 받은 값
      self.setMotorControl(pwm, IN1, IN2, speed, state)
      
  def run(self):
    try:
      print("모터 회전 앞으로 100프로 속도")
      time.sleep(0.3)
      self.setMotor(CH1, 100, FORWARD)
      self.client.publish("iot/dc", "100/front")
      time.sleep(14.5)
      self.setMotor(CH1, 0, STOP)
      print("모터 종료")
      self.client.publish("iot/raspberry/dc", "dc_motor_finish")
      self.client.publish("iot/android", "machine_finish")

    except RuntimeError as error:
      print(error.args[0])
    finally:
      pass


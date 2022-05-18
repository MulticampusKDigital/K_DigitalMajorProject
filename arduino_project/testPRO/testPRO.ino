#include <SoftwareSerial.h>
#include <Servo.h>

// 블루투스 HC-06
// 5V, GND
#define BT_RX 10
#define BT_TX 9
SoftwareSerial btSerial(BT_TX,BT_RX);

// 서보 모터
// 38 -> 직각 / 180 -> 호스 펴기
// 갈색 : GND, 빨강 : 5V, 주황 : SIG
#define SERVO_PIN 7
Servo myservo;

// 펌프 센서
// 모터 드라이버 사용 - MOTOR A 부분 사용(이 부분은 펌프 센서 선)
// VCC : 5V, GND : GND, A-1A : 6, A-1B : 5
#define AA 6
#define AB 5

// 물높이 센서
#define WATER_PIN A0
 
void setup() {
  Serial.begin(9600);
  // 블루투스
  btSerial.begin(9600);
  // 펌프 센서 핀 설정
  pinMode(AA, OUTPUT);
  pinMode(AB, OUTPUT);
  // 서보모터
  myservo.attach(SERVO_PIN);
  myservo.write(70);
}


void loop(){
  int level = analogRead(WATER_PIN);  // 수분센서의 신호를 측정합니다.
//  sendWaterHeightValue(level);
  myservo.detach();
  if (btSerial.available()) { // 블루투스로 다른 곳에서 입력받아온 것을 시리얼 모니터에서 확인가능
    String btVal = btSerial.readString();
    Serial.println(btVal);
    if (btVal == "on"){
      controlOnWater();
    } else if (btVal == "off"){
      controlOffWater();
    } else if (btVal == "dc_motor_finish"){
      controlServoMotor();
      myservo.detach();
    } 
  }
}

// 블루투스로 아두이노의 물높이 값 보내기 (아두이노 -> 라즈베리파이)
void sendWaterHeightValue(int level){
  // 600~700을 0~10단계로 물을 넣어서 측정할 수 있다. 하지만 10까지는 무리일 수도 8까지가 괜찮을 듯
  int levelNumber = map(level, 600, 700, 0, 10);
//  Serial.println(level);
//  Serial.println(levelNumber);
  btSerial.write(levelNumber);
  delay(100);
}


// 블루투스로 물 펌프 센서를 제어 (라즈베리파이 -> 펌프센서)
void controlOnWater(){
  digitalWrite(AA, HIGH);
  digitalWrite(AB, LOW);
}
void controlOffWater(){
  digitalWrite(AA, LOW);
  digitalWrite(AB, LOW);
}
// ===================================================

// 블루투스로 서보모터를 제어 (라즈베리파이 -> 서보모터)
void controlServoMotor(){
  myservo.attach(SERVO_PIN);
  delay(300);
  myservo.write(180);
  delay(20000);
  myservo.write(70);
  delay(300);
  myservo.detach();
  btSerial.write("all_finish");
}
// ===================================================

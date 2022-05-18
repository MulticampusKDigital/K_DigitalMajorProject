# Raspberry PI
## 설정 순서
1. SD 카드 포맷팅
2. 이미지 굽기
3. 연결된 드라이버에 ssh.txt 빈 파일과 wpa_supplicant.conf 파일 넣기(밑에 코드 내용으로 사용, 파일 생성 시 모든 파일형식 허용하기)
4. 22포트 IP 찾기 (IP SCANNER)
   - 여기서 22포트를 찾기 위해서 와이파이 비밀번호 없는 걸 사용하거나 5GHz와 같은 대역폭을 사용하는 와이파이는 잘 안된다.
5. VNC 확인 또는 PUTTY 확인하기
   1. Putty
      - 되도록 강제로 코드 뽑지 말고 shutdown하고 뽑기 
      - 아이디 : pi
      - 비밀번호 : raspberry
      - 종료
        ```
        sudo shutdown -h now
        ```
   ```
    country=US 
    ctrl_interface=DIR=/var/run/wpa_supplicant GROUP=netdev 
    update_config=1 
    network={ 
      ssid="WIFI 이름" 
      psk="WIFI 비밀번호" 
      scan_ssid=1 
    }
   ```

# 아두이노 - 라즈베리파이 블루투스 통신
현재 내가 가지고 있는 HC-06 MAC 주소 : 98:DA:20:03:82:47

- 필수 라이브러리 라즈베리파이 터미널에서 설치하기
```bash
sudo apt-get install bluetooth blueman bluez
sudo apt install bluez
sudo pip3 install pybluez pybleno

sudo reboot
```
- 블루투스 페어링
```bash
sudo bluetoothctl

power on
scan on

pair MAC주소 // 예시 : pair 98:D3:51:FD:E4:0C
trust MAC주소
```

# 새로운 환경에서 접속
1. VSCode에서 새롭게 접속되어지면 로딩이 뜬다.
2. 상단에 ssh config~ 라는 목록을 클릭 후
3. 파일경로를 선택한다.
4. 바뀐 ip를 적고
5. 저장하면 새롭게 바뀐 ip를 원격탐색기로 연다.
6. 탐색기 (파일확인하는 곳)에서 load file을 하고
7. 내가 저장한 Raspberry 파일 경로를 클릭한다.
8. 만약, Linux, Window, mac 이 뜨면 Linus -> continue를 클릭한다.
  
# Reference
[#1 gpio all 커맨드 입력 사용하기]<br>
https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=elepartsblog&logNo=220284169123<br>

https://reliablebull.tistory.com/entry/%EB%9D%BC%EC%A6%88%EB%B2%A0%EB%A6%AC%ED%8C%8C%EC%9D%B44-gpio-%EC%82%AC%EC%9A%A9

[#2 VSCode 에서 라즈베리파이 사용하기 위한 방법]<br>
https://blog.naver.com/heaves1

[#3 아두이노-라즈베리파이 블루투스 통신]<br>
https://popcorn16.tistory.com/196
https://namki-learning.tistory.com/8
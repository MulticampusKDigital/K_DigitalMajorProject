# 아두이노 -> 라즈베리파이
# 데이터 수신

from bluetooth import *
import struct

socket = BluetoothSocket( RFCOMM )
print("?")
socket.connect(("98:DA:20:03:82:47", 1))
print("bluetooth connected!")

while True:
    data = socket.recv(1024)
    # print(data)
    # 0~10까지의 값을 받아온다. 
    for d in data:
        if d in (0,1,2,3,4,5,6,7,8,9,10):
            print("값 : ", d)
            if d == 6:
                print("??")
                socket.send("dc motor on")
    # print("Received: %d" %data)
    if(data=="q"):
        print("Quit")
        break

socket.close()
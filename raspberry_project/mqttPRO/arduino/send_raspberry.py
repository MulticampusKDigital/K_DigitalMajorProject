# 라즈베리파이 -> 아두이노
# 데이터 전송

from bluetooth import *

socket = BluetoothSocket( RFCOMM )
socket.connect(("98:DA:20:03:82:47", 1))
print("bluetooth connected!")

msg = input("send message : ")
socket.send(msg)

print("finished")
socket.close()
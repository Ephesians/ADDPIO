import socket

class ADDPIO(object):

    global TIME_OUT,ATTEMPTS,DATA_SIZE,IO_PORT
    
    TIME_OUT = 3
    ATTEMPTS = 3
    DATA_SIZE = 1024
    IO_PORT = 6297
    
    # Android sensors
    SENSOR_ACCELEROMETER = 1
    SENSOR_AMBIENT_TEMPERATURE = 13
    SENSOR_GAME_ROTATION_VECTOR = 15
    SENSOR_GEOMAGNETIC_ROTATION_VECTOR = 20
    SENSOR_GRAVITY = 9
    SENSOR_GYROSCOPE = 4
    SENSOR_GYROSCOPE_UNCALIBRATED = 16
    SENSOR_HEART_BEAT = 31
    SENSOR_HEART_RATE = 21
    SENSOR_LIGHT = 5
    SENSOR_LINEAR_ACCELERATION = 10
    SENSOR_MAGNETIC_FIELD = 2
    SENSOR_MAGNETIC_FIELD_UNCALIBRATED = 14
    SENSOR_MOTION_DETECT = 30
    SENSOR_ORIENTATION = 3
    SENSOR_POSE_6DOF = 28
    SENSOR_PRESSURE = 6
    SENSOR_PROXIMITY = 8
    SENSOR_RELATIVE_HUMIDITY = 12
    SENSOR_ROTATION_VECTOR = 11
    SENSOR_SIGNIFICANT_MOTION = 17
    SENSOR_STATIONARY_DETECT = 29
    SENSOR_STEP_COUNTER = 19
    SENSOR_STEP_DETECTOR = 18
    SENSOR_TEMPERATURE = 7

    # Android input/output
    BUTTON_1 = 10001
    BUTTON_2 = 10002
    LED_RED = 10101
    LED_GREEN = 10102
    LED_BLUE = 10103
    ALARM = 10201
    NOTIFICATION = 10301
    TEXT = 10401
    TOUCH_PAD_X_IN = 10501
    TOUCH_PAD_Y_IN = 10502
    TOUCH_PAD_X_OUT = 10601
    TOUCH_PAD_Y_OUT = 10602

    def __init__(self, ipAddress):
        self.ipAddress = ipAddress
        self.port = IO_PORT
        
    def comm(self, direction, pin, value):
        complete = False
        count = 0
        # try ATTEMPTS times, then fail
        while not complete:
            try:
                sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
                sock.settimeout(TIME_OUT)
                message = direction + ":" + str(pin) + ":" + str(value)
                sock.sendto(message, (self.ipAddress, self.port))
                data, addr = sock.recvfrom(DATA_SIZE)
                sock.close()
                complete = True
            except socket.error:
                complete = False
                count = count + 1
                if count == ATTEMPTS:
                    complete = True
                    data = "comm fail"
        return data
                            
    def input(self, pin, value):
        return self.comm("in", pin, value)
                        
    def output(self, pin, value):
        return self.comm("out", pin, value)

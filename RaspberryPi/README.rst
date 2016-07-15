ADDPIO  project
==================

This project allows the Raspberry Pi* to access the sensors (accelerometer, gyroscope, ...)
and other IO of an Android* device, similar to the GPIO library. There is a corresponding
Android app (ADDPIO on the Google Play Store) to run on the Android device(s). The Raspberry
Pi and all Android devices must be connected to the same network. This uses UDP port 6297 to
communicate. Create a new ADDPIO object passing the ip address (this is displayed on the
Android app). The object has an input and output function that takes a type number and value.
See below for the standard type number symbols or use the number displayed on the Android app.
The Android sensors return an array of values (e.g. x,y,z). For ADDPIO sensor input the value
parameter represents the index into the array of values returned by the sensor. For other input,
the value is ignored.
The Android app has several widgets for IO:
buttons, LEDs, a touchpad, alarm, notification, and text.
Read the ip address and available sensors from the Android app.


from ADDPIO import ADDPIO
myHost = ADDPIO("192.168.0.0")
myValue = myHost.input(ADDPIO.SENSOR_ACCELEROMETER,1)
myValue = myHost.input(12345,47)
myHost.output(ADDPIO.ALARM,1)
myHost.output(ADDPIO.ALARM,0)


See the testADDPIO.py program for an example.

# Android sensors
SENSOR_ACCELEROMETER
SENSOR_AMBIENT_TEMPERATURE
SENSOR_GAME_ROTATION_VECTOR
SENSOR_GEOMAGNETIC_ROTATION_VECTOR
SENSOR_GRAVITY
SENSOR_GYROSCOPE
SENSOR_GYROSCOPE_UNCALIBRATED
SENSOR_HEART_BEAT
SENSOR_HEART_RATE
SENSOR_LIGHT
SENSOR_LINEAR_ACCELERATION
SENSOR_MAGNETIC_FIELD
SENSOR_MAGNETIC_FIELD_UNCALIBRATED
SENSOR_MOTION_DETECT
SENSOR_ORIENTATION
SENSOR_POSE_6DOF
SENSOR_PRESSURE
SENSOR_PROXIMITY
SENSOR_RELATIVE_HUMIDITY
SENSOR_ROTATION_VECTOR
SENSOR_SIGNIFICANT_MOTION
SENSOR_STATIONARY_DETECT
SENSOR_STEP_COUNTER
SENSOR_STEP_DETECTOR
SENSOR_TEMPERATURE

# Android input/output
BUTTON_1          input 0/1
BUTTON_2          input 0/1
LED_RED           output 0/1
LED_GREEN         output 0/1
LED_BLUE          output 0/1
ALARM             output 0/1
NOTIFICATION      output any number
TEXT              output any number
TOUCH_PAD_X_IN    input 0-255
TOUCH_PAD_Y_IN    input 0-255
TOUCH_PAD_X_OUT   output 0-255
TOUCH_PAD_Y_OUT   output 0-255


* Raspberry Pi is a trademark of the Raspberry Pi Foundation - http://www.raspberrypi.org
* Android is a trademark of Google Inc.

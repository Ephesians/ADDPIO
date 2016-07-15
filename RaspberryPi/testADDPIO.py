from ADDPIO import ADDPIO
import RPi.GPIO as GPIO
import sys  

#main function
if __name__ == "__main__":

    if(len(sys.argv) < 3) :
        print 'Usage : python testADDPIO.py ip_address1 ip_address2 (can be same ip address)'
        sys.exit()

    GPIO.setmode(GPIO.BCM)
    # don't need to have pins hooked up
    GPIO.setup(23, GPIO.IN)
    GPIO.setup(24, GPIO.IN)
    
    # will display the input from GPIO pin 23
    print "GPIO pin 23 = " + str(GPIO.input(23))
    # will display the input from GPIO pin 24
    print "GPIO pin 24 = " + str(GPIO.input(24))

    host1 = sys.argv[1]
    host2 = sys.argv[2]

    a1 = ADDPIO(host1)
    a2 = ADDPIO(host2)

    # will display '1' if button 2 on host1 is pressed, '0' otherwise
    print "host1 ADDPIO BUTTON_2 = " + a1.input(ADDPIO.BUTTON_2,0)
    # will display the input 1 from the accelerometer on host1
    print "host1 ADDPIO SENSOR_ACCELEROMETER[1] = " + a1.input(ADDPIO.SENSOR_ACCELEROMETER,1)
    # will display the input 1 from the pressure sensor on host1
    print "host1 ADDPIO SENSOR_PRESSURE[0] = " + a1.input(ADDPIO.SENSOR_PRESSURE,0)
    # will light the fake red LED on host1
    print "host1 set ADDPIO LED_RED = " + a1.output(ADDPIO.LED_RED,1)
    # will set the x value of the touch position on the touchpad region on host1
    print "host1 set ADDPIO TOUCH_PAD_X_OUT 128 = " + a1.output(ADDPIO.TOUCH_PAD_X_OUT,128)
    # will set the y value of the touch position on the touchpad region on host1
    print "host1 set ADDPIO TOUCH_PAD_Y_OUT 128 = " + a1.output(ADDPIO.TOUCH_PAD_Y_OUT,128)
    # will display the input 2 from the sensor type 1234 on host1
    print "host1 sensor 1234[2] = " + a1.input(1234,2)
    
    # will display '1' if button 1 on host2 is pressed, '0' otherwise
    print "host2 ADDPIO BUTTON_1 = " + a2.input(ADDPIO.BUTTON_1,0)
    # will display the input 2 from the gyroscope on host2
    print "host2 ADDPIO SENSOR_GYROSCOPE[2] = " + a2.input(ADDPIO.SENSOR_GYROSCOPE,2)
    # will display the input 0 from the orientation sensor on host2
    print "host2 ADDPIO SENSOR_ORIENTATION[0] = " + a2.input(ADDPIO.SENSOR_ORIENTATION,0)
    # will display the x value of the touch position on the touchpad region on host1
    print "host2 set ADDPIO TOUCH_PAD_X_IN = " + a2.input(ADDPIO.TOUCH_PAD_X_IN,0)
    # will display the y value of the touch position on the touchpad region on host1
    print "host2 set ADDPIO TOUCH_PAD_Y_IN = " + a2.input(ADDPIO.TOUCH_PAD_Y_IN,0)
    # will light the fake green LED on host2
    print "host2 set ADDPIO LED_GREEN = " + a2.output(ADDPIO.LED_GREEN,1)
    # will display "17" next to the text prompt on host2
    print "host2 set ADDPIO TEXT 17 = " + a2.output(ADDPIO.TEXT,17)
    # will display the input 0 from the sensor type 9999 on host2
    print "host2 sensor 9999[0] = " + a2.input(9999,0)

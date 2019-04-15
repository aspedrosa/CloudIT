import RPi.GPIO as GPIO
import time
import requests
import sys

# Pin number connected to the green, yellow and red corresponding leds.
GREEN_LED  = 3
YELLOW_LED = 5
RED_LED    = 7
# Map between the status name and the pin connected to the corresponding leds.
status_led_map = {"blue":GREEN_LED, "red":RED_LED, "":YELLOW_LED}

# Returns the status name for the latest build of the project
# with the name equal to the argument job_name
def get_jenkins_job_status(job_name):
    result = requests.get(url='http://192.168.160.63:8080/api/json?tree=jobs[name,color]',
                          auth=requests.auth.HTTPBasicAuth(
                               'admin',
                               '112b4d1426be54522d80aa8f101656f3ce')).json()
    for job in result["jobs"]:
        if job_name == job["name"]:
            return job["color"]
    return ""

# Main function. Will check the status name for the job given in the argument
# every 5 seconds and will light up the corresponding leds.
def main():
    if len(sys.argv) != 2:
        print("Usage: python " + sys.argv[0] + " <job_name>")
    GPIO.setmode(GPIO.BOARD)
    GPIO.setwarnings(False)
    GPIO.setup(RED_LED,GPIO.OUT)
    GPIO.setup(YELLOW_LED,GPIO.OUT)
    GPIO.setup(GREEN_LED,GPIO.OUT)
    job_name = sys.argv[1]
    current_build_status = None
    
    while 1:
        color = get_jenkins_job_status(job_name)
        if current_build_status != color or current_build_status == None: 
            if color in status_led_map:
                GPIO.output(status_led_map[color], GPIO.HIGH)
            else:
                GPIO.output(GREEN_LED, GPIO.LOW)
                GPIO.output(YELLOW_LED, GPIO.LOW)
                GPIO.output(GREEN_LED, GPIO.LOW)
        time.sleep(3)

main()

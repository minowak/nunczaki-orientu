import cv
import bluetooth

# discovering available devices (this may take a sec)
devices = bluetooth.discover_devices()

# print available devices
for dev in devices:
    print '%s: %s' % (dev, bluetooth.lookup_name(dev))
    services = bluetooth.find_service(address=dev)
    
    for i in services:
        print i
        
    print

capture = cv.CaptureFromCAM(0)

count = 0

while count < 25:
    image = cv.QueryFrame(capture)
    
    cv.ShowImage('Test WebCam in Python', image)
    cv.SaveImage('test_image.jpg', image)
    cv.WaitKey(2)
    count += 1
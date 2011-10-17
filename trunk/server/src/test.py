import cv

capture = cv.CaptureFromCAM(0)

count = 0

while count < 25:
    image = cv.QueryFrame(capture)
    
    cv.ShowImage('Test WebCam in Python', image)

    cv.WaitKey(2)
    count += 1
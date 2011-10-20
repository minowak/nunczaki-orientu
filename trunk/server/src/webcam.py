import cv
import threading
import gtk
import time

class WebCam(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        self.capture = cv.CaptureFromCAM(0)

    def run(self):
        while True:
            print 'looping kurwa'
            gtk.threads_enter()
            image = cv.QueryFrame(self.capture)
            gtk.threads_leave()
            
            cv.SaveImage('test_image.jpg', image)
            
if __name__ == '__main__':
    t = WebCam()
    t.start()
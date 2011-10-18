import cv
import bluetooth

import pygtk
pygtk.require('2.0')
import gtk

class Base:
    def search_devices(self, widget, data=None):
        devices = bluetooth.discover_devices()
        
        dbuff = ''
        
        for dev in devices:
            dbuff += str(dev) + " " + str(bluetooth.lookup_name(dev)) + '\n'
            
        textbuffer = self.devices_list.get_buffer()
        textbuffer.set_text(dbuff)
    
    def __init__(self):
        # VARIABLE INITS
        
        # GTK WINDOW INITS
        
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.window.set_title("Nunczaki Orientu")
        self.window.set_border_width(10)
        
        self.window.connect("delete_event", self.delete_event)
        self.window.connect("destroy", self.destroy)
    
        # BUTTONS etc
        
        self.button1 = gtk.Button("Search Devices")
        self.button1.connect("clicked", self.search_devices)
        self.devices_list = gtk.TextView()
    
        # LAYOUTS
        
        self.box1 = gtk.HBox(False, 0)
        self.window.add(self.box1)

        self.box1.pack_start(self.button1, True, True, 0)
        self.box1.pack_start(self.devices_list, True, True, 0)
        
        self.button1.show()
        self.box1.show()
        self.devices_list.show()
        
        self.window.show()
        print 'main window started'
        
    # EVENTS
        
    def delete_event(self, widget, event, data=None):
        print 'delete_event occured'
        gtk.main_quit()
        return True
    
    # CALLBACKS
    def destroy(self, widget, data=None):
        gtk.main_quit()
        
    def main(self):
        gtk.main()
        
if __name__ == '__main__':
    base = Base()
    base.main()

# discovering available devices (this may take a sec)
#devices = bluetooth.discover_devices()
#
## print available devices
#for dev in devices:
#    print '%s: %s' % (dev, bluetooth.lookup_name(dev))
#    services = bluetooth.find_service(address=dev)
#    
#    for i in services:
#        print i
#        
#    print
#    
## setting up connection
#server_sock = bluetooth.BluetoothSocket( bluetooth.RFCOMM)
#port = 8080
#
#server_sock.bind( ("", port) )
#server_sock.listen(1)
#
#client_sock, address = server_sock.accept()
#
#print "Accepted connection from ", address

#    capture = cv.CaptureFromCAM(0)
#
#    count = 0
#
#    while count < 25:
#        image = cv.QueryFrame(capture)
#    
#        cv.ShowImage('Test WebCam in Python', image)
#        cv.SaveImage('test_image.jpg', image)
#        cv.WaitKey(2)
#        count += 1
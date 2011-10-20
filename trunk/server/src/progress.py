from webcam import WebCam
import bluetooth
import gtk
import threading
import time

class ProgressThread(threading.Thread):
    def __init__(self, devs):
        threading.Thread.__init__(self)
       # gtk.threads_init()
        self.base = devs
        
    def run(self):
        gtk.threads_enter()
        self.base.liststore.clear()
        print 'starting searching thread'
        self.base.statusbar.push(0, 'Searching ...')
        try:
            devices = bluetooth.discover_devices()
            
            print 'end searching'
        
            found = 0
        
            for dev in devices:
                found += 1
                self.base.liststore.append([str(bluetooth.lookup_name(dev)), str(dev)])
            
                print 'showing devices'

                self.base.button1.set_sensitive(True)
        
            if found > 0:
                self.base.statusbar.push(0, 'Found ' + str(found) + ' devices')
            else:
                self.base.statusbar.push(0, 'No devices found')
        except:
            print 'not found bluetooth adapter'
            self.base.statusbar.push(0, 'No Bluetooth adapter connected')
            self.base.button1.set_sensitive(True)
        finally:
            gtk.threads_leave()
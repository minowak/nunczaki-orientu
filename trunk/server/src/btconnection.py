import bluetooth
import gtk
from webcam import WebCam

class BluetoothConnection:
    def __init__(self, address):
        self.address = address
        
    def establish_connection(self):
        gtk.threads_enter()
        try:
            self.server_sock = bluetooth.BluetoothSocket( bluetooth.RFCOMM)
        
            self.port = 1
            self.server_sock.connect( (self.address, self.port))
            
            # START SCREENCAPPING THREAD
            
            t = WebCam()
            t.start()
        
        except:
            return False
        finally:
            gtk.threads_leave()
            self.server_sock.close()
        
        return True
        
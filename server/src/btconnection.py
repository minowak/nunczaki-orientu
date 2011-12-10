import bluetooth
import gtk
from webcam import WebCam

class BluetoothConnection:
    def __init__(self, address):
        self.address = address
        
    def establish_connection(self):
    	print 'dupa dupa dupa'
        #gtk.threads_enter()
	try:
	    self.server_sock = bluetooth.BluetoothSocket( bluetooth.RFCOMM)
        
            self.port = 1
	    uuid = "1e0ca4ea-299d-4335-93eb-27fcfe7fa848"
	    service_matches = bluetooth.find_service( uuid = uuid)

	    if len(service_matches) == 0:
	        print "coudlnt find the service"
		return False

	    first_match = service_matches[0]
	    self.port = first_match["port"]
	    print "connecting to ", self.address
            self.server_sock.connect( (self.address, self.port))
	    print "connected"
	    # self.send("Hello")
	    # TODO send webcam
        except:
            return False
        finally:
         #   gtk.threads_leave()
            self.server_sock.close()
        
        return True
        

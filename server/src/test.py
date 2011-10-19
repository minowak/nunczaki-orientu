import cv
import bluetooth

import pygtk
pygtk.require('2.0')
import gtk

class Base:
    def search_devices(self, widget, data=None):
        devices = bluetooth.discover_devices()
        
        for dev in devices:
            self.liststore.append([str(bluetooth.lookup_name(dev)), str(dev)])
    
    def __init__(self):
        # VARIABLE INITS
        
        # GTK WINDOW INITS
        
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.window.set_title("Nunczaki Orientu")
        self.window.set_border_width(10)
        self.window.set_size_request(300, 400)
        
        self.window.connect("delete_event", self.delete_event)
        self.window.connect("destroy", self.destroy)
    
        # BUTTONS etc
        
        self.button1 = gtk.Button("Search Devices")
        self.button1.connect("clicked", self.search_devices)
        
        self.liststore = gtk.ListStore( str, str )
        self.treeview = gtk.TreeView(self.liststore)
        
        self.tvcolumn = gtk.TreeViewColumn('Device name')
        self.tvcolumn2 = gtk.TreeViewColumn('Address')
        
        self.treeview.append_column(self.tvcolumn)
        self.treeview.append_column(self.tvcolumn2)
        
        self.cell = gtk.CellRendererText()
    
        self.tvcolumn.pack_start(self.cell, True)
        self.tvcolumn2.pack_start(self.cell, True)
        
        self.tvcolumn.set_attributes(self.cell, text=0)
        self.tvcolumn2.set_attributes(self.cell, text=1)
    
        # LAYOUTS
        
        self.box1 = gtk.VBox(False, 0)
        self.window.add(self.box1)

        self.vscrollbar = gtk.ScrolledWindow()
        self.vscrollbar.set_policy(gtk.POLICY_NEVER, gtk.POLICY_AUTOMATIC)
        self.vscrollbar.add(self.treeview)

        self.box1.pack_start(self.vscrollbar, True, True, 0)
        self.box1.pack_start(self.button1, True, True, 0)
        
#        self.button1.show()
#        self.box1.show()
#        self.devices_list.show()
        
        self.window.show_all()
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
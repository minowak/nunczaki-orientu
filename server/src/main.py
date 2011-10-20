from btconnection import BluetoothConnection
from progress import ProgressThread
from webcam import WebCam
import cv
import gtk
gtk.threads_init()
import os
import pygtk
pygtk.require('2.0')


# CLASSES
        
class Base: 
    def start_streaming(self, widget, data=None):
        print 'started streaming'
        self.button3.set_sensitive(False)
        self.button4.set_sensitive(True)
        self.statusbar.push(0, 'Streaming . . .')
        
    def stop_streaming(self, widget, data=None):
        print 'stopped streaming'
        self.button3.set_sensitive(True)
        self.button4.set_sensitive(False)
        self.statusbar.push(0, 'Stopped streaming')
        
    def device_selected(self, selection):   
        self.button2.set_sensitive(True)    
            
        sel = self.treeview.get_selection()
        sel.set_mode(gtk.SELECTION_SINGLE)
        tm, ti = sel.get_selected()
        
        if ti is not None:
            self.dev_address = str(tm.get_value(ti, 1))
            self.statusbar.push(0, 'Selected device ' + str(tm.get_value(ti, 0)) + ' with MAC: ' + self.dev_address)
        else:
            print 'iter is none'  
        return True   
    
    def device_connect(self, widget, data=None):
        print 'connecting'

        btconnect = BluetoothConnection(self.dev_address)
        self.is_connected = btconnect.establish_connection()
        if self.is_connected is True:
            self.statusbar.push(0, 'Connected')
            
            self.t = WebCam()
            self.t.start()
            
            self.button3.set_sensitive(True)
            
        else:
            self.statusbar.push(0, "Can't establish connection")
           
    def search_devices(self, widget, data=None):
        self.button1.set_sensitive(False)        
        progress_thread = ProgressThread(self)
        progress_thread.start()
    
    def __init__(self):
        # VARIABLE INITS

        # GTK WINDOW INITS
        
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.window.set_title("Nunczaki Orientu")
        self.window.set_border_width(10)
        self.window.set_size_request(450, 400)
        self.window.set_position(gtk.POS_RIGHT)
        
        self.window.connect("delete_event", self.delete_event)
        self.window.connect("destroy", self.destroy)
    
        # BUTTONS
        
        self.button1 = gtk.Button("Search Devices")
        self.button1.connect("clicked", self.search_devices)
        
        self.button2 = gtk.Button("Connect")
        self.button2.connect("clicked", self.device_connect)
        self.button2.set_sensitive(False)
        
        self.button3 = gtk.Button("Start streaming")
        self.button3.set_sensitive(False)
        self.button3.connect("clicked", self.start_streaming)
        
        self.button4 = gtk.Button("Stop streaming")
        self.button4.set_sensitive(False)
        self.button4.connect("clicked", self.stop_streaming)
        
        # LISTVIEW
        
        self.liststore = gtk.ListStore( str, str )
        self.treeview = gtk.TreeView(self.liststore)
        
        self.tvcolumn = gtk.TreeViewColumn('Device name                   ') # lol i don't wanna search for another solution :P
        self.tvcolumn2 = gtk.TreeViewColumn('Address')
        
        self.treeview.append_column(self.tvcolumn)
        self.treeview.append_column(self.tvcolumn2)
        
        cell = gtk.CellRendererText()
    
        self.tvcolumn.pack_start(cell, True)
        self.tvcolumn2.pack_start(cell, True)
        
        self.tvcolumn.set_attributes(cell, text=0)
        self.tvcolumn2.set_attributes(cell, text=1)
    
        self.treeview.get_selection().connect("changed", self.device_selected)
        
        # STATUSBAR
        
        self.statusbar = gtk.Statusbar()
        self.statusbar.push(0, 'Welcome')
    
        # LAYOUTS
        
        self.box1 = gtk.VBox(False, 0)
        self.box2 = gtk.HBox(False, 0)
        self.window.add(self.box1)

        self.vscrollbar = gtk.ScrolledWindow()
        self.vscrollbar.set_policy(gtk.POLICY_NEVER, gtk.POLICY_AUTOMATIC)
        self.vscrollbar.add(self.treeview)

        self.box2.pack_start(self.button1, False, True, 0)
        self.box2.pack_start(self.button2, False, True, 0)
        self.box2.pack_start(self.button3, False, True, 0)
        self.box2.pack_start(self.button4, False, True, 0)
        
        self.box1.pack_start(self.vscrollbar, True, True, 0)
        self.box1.pack_start(self.box2, False, True, 0)
        self.box1.pack_start(self.statusbar, False, True, 0)
        
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

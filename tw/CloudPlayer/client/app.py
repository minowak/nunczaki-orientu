'''
Created on 02-12-2011

@author: News
'''

import pygtk
pygtk.require('2.0')
import gtk

class AppBase:
    # Callbacks
    def destroy(self, widget, data=None):
        gtk.main_quit()
        
    def play_callback(self, widget, data=None): 
        print "Play"
        
    def stop_callback(self, widget, data=None):
        print "Stop"
    
    def initGUI(self):
        self.HBox = gtk.HBox(False, 0)
        
        self.playBtn = gtk.Button("Play")
        self.stopBtn = gtk.Button("Stop")
        
        # Connects
        self.playBtn.connect_object("clicked", self.play_callback, None)
        self.stopBtn.connect_object("clicked", self.stop_callback, None)
        
        self.HBox.add(self.playBtn)
        self.HBox.add(self.stopBtn)
        self.HBox.show_all()
        
    def __init__(self):
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.window.set_title("Cloud PLayer")
        self.window.show()
        
        self.initGUI()
        
        self.window.add(self.HBox)
        self.window.set_border_width(5)
        self.window.connect("destroy", self.destroy)
        
    def main(self):
        gtk.main()

def main():
    base = AppBase()
    base.main()

if __name__=='__main__':
    main()
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
        
    def connect_callback(self, widget, data=None):
        print "Connecting: " + self.serverTextBox.get_text()
        
    def initGUI(self):
        # Boxes
        self.HBox = gtk.HBox(False, 10)
        self.serverHBox = gtk.HBox(False, 5)
        self.VBox = gtk.VBox(False, 5)
        
        # Images
        playImg = gtk.Image()
        playImg.set_from_file("play.png")
        playImg.show()
        
        stopImg = gtk.Image()
        stopImg.set_from_file("stop.png")
        stopImg.show()
        
        # Buttons
        self.playBtn = gtk.Button()
        self.playBtn.add(playImg)
        self.playBtn.set_size_request(40, 40)
        
        self.stopBtn = gtk.Button()
        self.stopBtn.add(stopImg)
        self.stopBtn.set_size_request(40, 40)
        
        self.connectBtn = gtk.Button("Connect")
        
        # Text Box
        self.serverTextBox = gtk.Entry()
        
        # Scale
        self.scale = gtk.HScale()
        self.scale.set_digits(2)
        self.scale.set_draw_value(0)
        self.scale.set_size_request(250, 10)
        self.scale.show()
        
        # Connects
        self.playBtn.connect_object("clicked", self.play_callback, None)
        self.stopBtn.connect_object("clicked", self.stop_callback, None)
        self.connectBtn.connect_object("clicked", self.connect_callback, None)
        
        # Packing
        self.HBox.pack_start(self.playBtn, False, False, 0)
        self.HBox.pack_start(self.stopBtn, False, False, 0)
        self.HBox.add(self.scale)
        self.HBox.show_all()
        
        self.serverHBox.pack_start(self.connectBtn, False, False, 0)
        self.serverHBox.add(self.serverTextBox)
        
        self.VBox.pack_start(self.HBox, False, False, 5)
        self.VBox.pack_start(self.serverHBox, False, False, 5)
        self.VBox.show_all();
        
    def __init__(self):
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.window.set_default_size(20,20)
        self.window.set_title("Cloud PLayer")
        self.window.show()
        
        self.initGUI()
        
        self.window.add(self.VBox)
        self.window.set_border_width(5)
        self.window.connect("destroy", self.destroy)
        
    def main(self):
        gtk.main()

def main():
    base = AppBase()
    base.main()

if __name__=='__main__':
    main()
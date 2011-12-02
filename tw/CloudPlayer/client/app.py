'''
Created on 02-12-2011

@author: News
'''

import thread
import gtk
import pygame
import pygtk
pygtk.require('2.0')


def play_music(music_file):
    """
    stream music with mixer.music module in blocking manner
    this will stream the sound from disk while playing
    """
    clock = pygame.time.Clock()
    try:
        pygame.mixer.music.load(music_file)
        print "Music file %s loaded!" % music_file
    except pygame.error:
        print "File %s not found! (%s)" % (music_file, pygame.get_error())
        return
    pygame.mixer.music.play()
    while pygame.mixer.music.get_busy():
        # check if playback has finished
        clock.tick(30)

class AppBase:
    # Callbacks
    def destroy(self, widget, data=None):
        gtk.main_quit()
        
    def play_callback(self, widget, data=None): 
        print "Play"
        # set up the mixer
        freq = 44100     # audio CD quality
        bitsize = -16    # unsigned 16 bit
        channels = 2     # 1 is mono, 2 is stereo
        buffer = 2048    # number of samples (experiment to get right sound)
        pygame.mixer.init(freq, bitsize, channels, buffer)

            # optional volume 0 to 1.0
        pygame.mixer.music.set_volume(0.75)
        # TODO W¹tki w GTk
        thread.start_new_thread(play_music, ("Kalimba.mp3", ))
        
        
    def stop_callback(self, widget, data=None):
        print "Stop2"
        
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
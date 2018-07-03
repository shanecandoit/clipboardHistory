#from Tkinter import *
import tkinter

'''shane 2012 gpl3 or greater'''

class Board(object):
    '''a text widget that sits on top of other windows and holds your clipboard
    history'''
    def __init__(self):
        self.root = tkinter.Tk()
        self.root.title('Clip History')
        self.root.wm_attributes('-topmost', 1)
        self.tx = tkinter.Text(self.root)
        self.tx.configure(font=('new courier',9),width=30,height=15)
        self.tx.pack(fill='both',expand=1)
        
        self.cont=''
        self.getClip()
        #self.loop()
        
    def getClip(self):
        #print 'get clip'
        try:
            self.clip = self.tx.selection_get(selection='CLIPBOARD')
        except:
            pass
        if not self.cont == self.clip:
            print('new clip text', self.clip)
            self.cont=self.clip
            try:
                self.tx.insert(END,'\n'+self.cont)
            except:
                pass
        self.root.after(1000,self.getClip)

    #def loop(self):
        #tkinter.mainloop()

b=Board()
tkinter.mainloop()

package game.util;


import java.awt.event.*;

public class InputTeclado implements KeyListener {
   
   private boolean[] teclas;
   private int[] polled;
   
   public InputTeclado() {
      teclas = new boolean[ 256 ];
      polled = new int[ 256 ];
   }

   public boolean keyDown( int keyCode ) {
      return polled[ keyCode ] > 0;
   }
   
   public boolean keyDownOnce( int keyCode ) {
      return polled[ keyCode ] == 1;
   }
   
   public synchronized void poll() {
      for( int i = 0; i < teclas.length; ++i ) {
         if( teclas[i] ) {
            polled[i]++;
         } else {
            polled[i] = 0;
         }
      }
   }

   public synchronized void keyPressed( KeyEvent e ) {
      int keyCode = e.getKeyCode();
      if( keyCode >= 0 && keyCode < teclas.length ) {
         teclas[ keyCode ] = true;
      }
   }

   public synchronized void keyReleased( KeyEvent e ) {
      int keyCode = e.getKeyCode();
      if( keyCode >= 0 && keyCode < teclas.length ) {
         teclas[ keyCode ] = false;
      }
   }

   public void keyTyped( KeyEvent e ) {
      // Para implementar KeyListener
   }
}

package game.util;

//Clase que cuenta el tiempo que tarda en hacer render cada segundo y lo muestra
public class FrameRate {
   
   private String frameRate;
   private long lastTime;
   private long delta;
   private int frameCount;

   //Inicializa las variables
   public void initialize() {
      lastTime = System.currentTimeMillis();
      frameRate = "FPS 0";
   }
   /*Para calcular la velocidad de fotogramas, 
    * el tiempo actual se resta de la última vez y 
    * se almacena en la variable delta. El contador de 
    * fps se incrementa cada fotograma, y cuando 
    * el tiempo delta es superior a un segundo,
    * se generan los nuevos FPS.
    * Dado que la variable delta rara vez es exactamente 
    * un segundo, 1.000 milisegundos se restan de la
    * variable delta para guardar los milisegundos 
    * adicionales. Una vez que se guarda la nueva
    * velocidad de fps, se restablece el 
    * recuento de fotogramas y se inicia de nuevo el proceso.*/
   public void calculate() {
      long current = System.currentTimeMillis();
      delta += current - lastTime;
      lastTime = current;
      frameCount++;
      if( delta > 1000 ) {
         delta -= 1000;
         frameRate = String.format( "FPS %s", frameCount );
         frameCount = 0;
      }
   }
   
   public String getFrameRate() {
      return frameRate;
   }
}

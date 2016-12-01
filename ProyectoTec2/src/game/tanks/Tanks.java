package game.tanks;
//Importando paquetes de Java
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import game.util.*;
import javax.swing.*;

//Clase extendiendo Jframe y Runnable. M�todo Main hasta abajo
public class Tanks extends JFrame implements Runnable {
	
	//Declaraci�n de las variables de clase
	private static final long serialVersionUID = 1L;
	private FrameRate frameRate;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private InputMouse mouse;
	private InputTeclado keyboard;
	private Canvas canvas;
	private Vectores[] mapaPiso;
	private Vectores[] mapaPisoCpy;
	private Vectores[] tanqueA;
	private Vectores[] tanqueB;
	private Vectores[] tanqueACpy;
	private Vectores[] tanqueBCpy;
	private Vectores[] cannon;
	private Vectores[] cannon2;
	private Vectores[] cannonCpy;
	private Vectores[] cannonCpy2;
	private float cannonRot, cannonDelta;
	private float cannonRot2, cannonDelta2;
	private Vectores bullet;
	private Vectores bullet2;
	private Vectores bulletCpy;
	private Vectores bulletCpy2;
	private Vectores velocity;
	private Vectores velocity2;
	public float viento;

	public Tanks() {
		
	}
	//Aqu� se define el tama�o de la ventana y sus par�metros
	protected void createAndShowGUI() {
		canvas = new Canvas(); 
		canvas.setSize(1240, 480); //Aqu� se define el tama�o de la ventana (x, y)
		canvas.setBackground(Color.BLACK); //El color del fondo
		//Inicializaci�n de canvas
		canvas.setIgnoreRepaint(false);
		getContentPane().add(canvas);
		setTitle("Pelea de Tanques"); //Nombre de la ventana
		setIgnoreRepaint(false);
		//Pack hace que los valores de arriba se apliquen
		pack();
		//Valores a las variables declaradas arriba
		keyboard = new InputTeclado(); //Teclado
		canvas.addKeyListener(keyboard); //KeyListener
		mouse = new InputMouse(canvas); //Mouse
	
		setVisible(true); //Hace visible la ventana
		canvas.createBufferStrategy(2); //Creaci�n de 2 Buffers (Double Buffer)
		bs = canvas.getBufferStrategy(); //Pasando el valor a variable bs
		canvas.requestFocus(); //Hace que el buffer 1 tenga prioridad
		gameThread = new Thread(this); //Thread hace que haya varios 'hilos' 
		gameThread.start();//Ejecuta el thread
	}
	//Metodo de ejecuci�n del juego
	public void run() {
		running = true;
		initialize(); //Llama al m�todo que inicializa todos los objetos
		//Estos valores hacen que exista 'tiempo' dentro del juego
		long curTime = System.nanoTime(); //Tiempo en nanosegundos 
		long lastTime = curTime;
		double nsPerFrame; //Nanosegundos por cuadro
		//Mientras running sea verdadero el juego se ejecuta en su loop
		while (running) {
			curTime = System.nanoTime();
			nsPerFrame = curTime - lastTime;
			//Se llama al loop con el parametro del tiempo en segundos. 
			gameLoop(nsPerFrame / 1.0E9); 
			lastTime = curTime;
		}
	}
	//Inicializaci�n de todos los objetos
	private void initialize() {
		//Cuenta el tiempo que tarda en hacer render cada segundo
		frameRate = new FrameRate(); 
		frameRate.initialize(); //Inicializa el frameRAte
		//Crea los objetos de velocidad de los 2 proyectiles
		velocity = new Vectores(); 
		velocity2 = new Vectores();
		viento = 0.0f; //La fuerza inicial del viento inicializa en 0
		//La rotaci�n de los ca�ones se inicializa en el 'angulo' 0
		cannonRot = 0.0f;
		cannonRot2 = 0.0f;
		//La velocidad de cambio de la rotaci�n de los ca�ones se inicializa
		cannonDelta = (float) Math.toRadians(180);
		cannonDelta2 = (float) Math.toRadians(180);
		//Usando la clase Vectores se le est� asignando coordenadas a 
		//diferentes puntos y conectando el punto inicial con el 
		//�ltimo punto dibujado. As� creando un pol�gono cerrado
		mapaPiso = new Vectores[] {new Vectores(0f, 0f), new Vectores(0.0f,1.0f),
				new Vectores(1.0f,1.0f), new Vectores(1.0f, 0.0f)};
		tanqueA = new Vectores[] {new Vectores(1f, 0f), new Vectores(.8f,.5f),
				new Vectores(.2f,.5f), new Vectores(0f, 0f)};
		cannon = new Vectores[] {new Vectores(1.2f, .3f), new Vectores(1.2f,.5f),
				new Vectores(1.0f,.4f), new Vectores(.0f, .0f)};
		tanqueB = new Vectores[] {new Vectores(1f, 0f), new Vectores(.8f,.5f),
				new Vectores(.2f,.5f), new Vectores(0f, 0f)};
		cannon2 = new Vectores[] {new Vectores(-1.2f, .3f), new Vectores(-1.2f,.5f),
				new Vectores(-1.0f,.4f), new Vectores(.0f, .0f)
		};
		/*Para poder tener un buffer doble se necesita que existan
		 *2 copias del mismo 'dibujo' hecho en las matrices de Vectores
		 *As�, los objetos se actualizan en el fondo y se imprimen las copias
		 *Aqu� se le asigna el tama�o de la matriz a la copia */
		mapaPisoCpy = new Vectores[mapaPiso.length];
		cannonCpy = new Vectores[cannon.length];
		cannonCpy2 = new Vectores[cannon2.length];
		tanqueACpy = new Vectores[tanqueA.length];
		tanqueBCpy = new Vectores[tanqueB.length];
		/*Se escalan los dibujos usando el m�todo scale en la clase
		 * Matrices. Esto es para que el mapa y los objectos tengan
		 * una sensaci�n de mayor distancia */
		//El mapa escala a 75% 
		Matrices scaleMapa = Matrices.scale(.75f, .75f);
		for (int i = 0; i < mapaPiso.length; ++i) {
			mapaPiso[i] = scaleMapa.mul(mapaPiso[i]);
		}		
		//Todo lo demas se escala a un 50%
		Matrices scale = Matrices.scale(.5f, .5f); //Estos son los valores de escala
		for (int i = 0; i < cannon.length; ++i) {
			cannon[i] = scale.mul(cannon[i]);
		}		
		Matrices scale2 = Matrices.scale(.5f, .5f);
		for (int i = 0; i < cannon2.length; ++i) {
			cannon2[i] = scale2.mul(cannon2[i]);
		}		
		Matrices scaleA = Matrices.scale(.5f, .5f);
		for (int i = 0; i < tanqueA.length; ++i) {
			tanqueA[i] = scaleA.mul(tanqueA[i]);
		}	
		Matrices scaleB = Matrices.scale(.5f, .5f);
		for (int i = 0; i < tanqueB.length; ++i) {
			tanqueB[i] = scaleB.mul(tanqueB[i]);
		}
	}
	//�ste es el loop del juego pasando el parametro de tiempo en nS
	private void gameLoop(double delta) {
		processInput(delta); //Input de mouse y teclado en loop
		updateObjects(delta); //La actualizaci�n de objectos 
		renderFrame(); 
		sleep(10L);
	}

	private void renderFrame() {
		do {
			do {
				Graphics g = null;
				try {
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					render(g);
				} finally {
					if (g != null) {
						g.dispose();
					}
				}
			} while (bs.contentsRestored());
			bs.show();
		} while (bs.contentsLost());
	}

	private void sleep(long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException ex) {
		}
	}

	private void processInput(double delta) {
		keyboard.poll();
		mouse.poll();
		if (keyboard.keyDown(KeyEvent.VK_W)) {
			cannonRot += cannonDelta * delta;
		}
		if (keyboard.keyDown(KeyEvent.VK_S)) {
			cannonRot -= cannonDelta * delta;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_SPACE)) {
			// new velocity
			viento = (float)Math.random()* 2;
			Matrices mat = Matrices.translate(10.5f + viento , 0.0f);
			mat = mat.mul(Matrices.rotate(cannonRot));
			velocity = mat.mul(new Vectores());
			// place bullet at cannon end
			mat = Matrices.translate(0.45f, 0.20f);
			mat = mat.mul(Matrices.rotate(cannonRot));
			mat = mat.mul(Matrices.translate(-1.6f, -1.75f));
			bullet = mat.mul(new Vectores());
		}
		if (keyboard.keyDown(KeyEvent.VK_J)) {
			cannonRot2 += cannonDelta2 * delta;
		}
		if (keyboard.keyDown(KeyEvent.VK_U)) {
			cannonRot2 -= cannonDelta2 * delta;
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_I)) {
			// new velocity
			Matrices mat2 = Matrices.translate(-10.5f, 0.0f);
			mat2 = mat2.mul(Matrices.rotate(cannonRot2));
			velocity2 = mat2.mul(new Vectores());
			// place bullet at cannon end
			mat2 = Matrices.translate(-.50f, 0.20f);
			mat2 = mat2.mul(Matrices.rotate(cannonRot2));
			mat2 = mat2.mul(Matrices.translate(10.5f, -1.75f));
			bullet2 = mat2.mul(new Vectores());
		}
	}

	private void updateObjects(double delta) {
		Matrices matMapa = Matrices.identity();
		Matrices mat = Matrices.identity();
		Matrices mat2 = Matrices.identity();
		Matrices matA = Matrices.identity();
		Matrices matB = Matrices.identity();
		matMapa = matMapa.mul(Matrices.translate(-0.385f, -1.718f));
		mat = mat.mul(Matrices.rotate(cannonRot));
		mat = mat.mul(Matrices.translate(-1.6f, -1.75f));
		matA = matA.mul(Matrices.translate(-2.0f, -2.0f));
		mat2 = mat2.mul(Matrices.rotate(cannonRot2));
		mat2 = mat2.mul(Matrices.translate(2.1f, -1.75f));
		matB = matB.mul(Matrices.translate(2.0f, -2.0f));
		for (int i = 0; i < mapaPiso.length; ++i) {
			mapaPisoCpy[i] = matMapa.mul(mapaPiso[i]);
		}
		for (int i = 0; i < tanqueA.length; ++i) {
			tanqueACpy[i] = matA.mul(tanqueA[i]);
		}
		for (int i = 0; i < tanqueB.length; ++i) {
			tanqueBCpy[i] = matB.mul(tanqueB[i]);
		}
		for (int i = 0; i < cannon.length; ++i) {
			cannonCpy[i] = mat.mul(cannon[i]);
		}
		for (int i = 0; i < cannon2.length; ++i) {
			cannonCpy2[i] = mat2.mul(cannon2[i]);
		}
		if (bullet != null) {
			velocity.y += -9.0f * delta;
			bullet.x += velocity.x * delta;
			bullet.y += velocity.y * delta;
			bulletCpy = new Vectores(bullet);
			if (bullet.y < -2.033f || bullet.x > 14.2f || bullet.x < -4.5f ) {
				bullet = null;
			}
		}
		if (bullet2 != null) {
			velocity2.y += -9.0f * delta;
			bullet2.x += velocity2.x * delta;
			bullet2.y += velocity2.y * delta;
			bulletCpy2 = new Vectores(bullet2);
			if (bullet2.y < -2.033f || bullet2.x > 14.2f || bullet2.x < -4.6f) {
				bullet2 = null;
			}
		}
	}

	private void render(Graphics g) {
		g.setColor(Color.GREEN);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
		frameRate.calculate();
		g.drawString(frameRate.getFrameRate(), 20, 25);
		g.drawString("(W) / (S) to raise/lower the leftCannon and (U) / (J) to raise/lower the rightCannon", 20, 50);
		g.drawString("Press Space to fire leftTank and Press I to fire rightTank", 20, 75);
		String vel = String.format("Velocity (%.2f,%.2f)", velocity.x,
				velocity.y);
		g.drawString(vel, 20, 100);
		String vel2 = String.format("Velocity2 (%.2f,%.2f)", velocity2.x,
				velocity2.y);
		g.drawString(vel2, 20, 130);
		String vientoString = String.format("Viento (%.2f)", viento);
		g.drawString(vientoString, 20, 160);
		float worldWidth = 20.0f;
		float worldHeight = 5.0f;
		float screenWidth = canvas.getWidth() - 1;
		float screenHeight = canvas.getHeight() - 1;
		float sx = screenWidth / worldWidth;
		float sy = -screenHeight / worldHeight ;
		float sxMapa = screenWidth/ worldWidth * 25.0f ;
		float syMapa = -screenHeight/ worldHeight + 5;
		Matrices viewport = Matrices.scale(sx, sy);
		Matrices viewport2 = Matrices.scale(sx, sy);
		Matrices viewportMapa = Matrices.scale(sxMapa, syMapa);
		float tx = screenWidth / 4.0f;
		float ty = screenHeight / 2.0f;
		float tx2 = screenWidth / 1.5f;
		float ty2 = screenHeight / 2.0f;
		float txMapa = screenWidth / 2.0f;
		float tyMapa = screenHeight / 1.4f;
		viewport = viewport.mul(Matrices.translate(tx, ty));
		viewport2 = viewport2.mul(Matrices.translate(tx2, ty2));
		viewportMapa = viewportMapa.mul(Matrices.translate(txMapa, tyMapa));
		for (int i = 0; i < cannon.length; ++i) {
			cannonCpy[i] = viewport.mul(cannonCpy[i]);
		}
		for (int i = 0; i < tanqueA.length; ++i) {
			tanqueACpy[i] = viewport.mul(tanqueACpy[i]);
		}
		for (int i = 0; i < cannon2.length; ++i) {
			cannonCpy2[i] = viewport2.mul(cannonCpy2[i]);
		}
		for (int i = 0; i < tanqueB.length; ++i) {
			tanqueBCpy[i] = viewport2.mul(tanqueBCpy[i]);
		}
		for (int i = 0; i < mapaPiso.length; ++i) {
			mapaPisoCpy[i] = viewportMapa.mul(mapaPisoCpy[i]);
		}
		dibujaPoligono(g, mapaPisoCpy);
		dibujaPoligono(g, cannonCpy);
		dibujaPoligono(g, cannonCpy2);
		dibujaPoligono(g, tanqueACpy);
		dibujaPoligono(g, tanqueBCpy);
		if (bullet != null) {
			bulletCpy = viewport.mul(bulletCpy);
			g.drawRect((int) bulletCpy.x - 2, (int) bulletCpy.y - 2, 4, 4);
		}
		if (bullet2 != null) {
			bulletCpy2 = viewport.mul(bulletCpy2);
			g.drawRect((int) bulletCpy2.x - 2, (int) bulletCpy2.y - 2, 4, 4);
		}
	}

	private void dibujaPoligono(Graphics g, Vectores[] poligono) {
		Vectores P;
		Vectores S = poligono[poligono.length - 1];
		for (int i = 0; i < poligono.length; ++i) {
			P = poligono[i];
			g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
			S = P;
		}
	}

	protected void onWindowClosing() {
		try {
			running = false;
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		final Tanks app = new Tanks();
		app.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.onWindowClosing();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createAndShowGUI();
			}
		});
	}
}
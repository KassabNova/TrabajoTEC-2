package game.util;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import game.util.FrameRate;
import game.util.InputTeclado;
import game.util.Matrices;
import game.util.InputMouse;
import game.util.Utilidad;
import game.util.Vectores;

public class Framework extends JFrame implements Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	protected FrameRate frameRate;
	protected Canvas canvas;
	protected InputMouse mouse;
	protected InputTeclado teclado;
	protected Color appFondo = Color.BLACK;
	protected Color appMargen = Color.LIGHT_GRAY;
	protected Color appFPSColor = Color.GREEN;
	protected Font appFont = new Font("Courier New", Font.PLAIN, 14);
	protected String appTitulo = "TBD-Title";
	protected float appMargenEscala = 0.8f;
	protected int appAncho = 640;
	protected int appAlto = 480;
	protected float appMundoAncho = 2.0f;
	protected float appMundoAlto = 2.0f;
	protected long appSleep = 10L;
	protected boolean appMantenerRatio = false;
	protected int textPos = 0; 

	public Framework() {
		
	}

	protected void createAndShowGUI() {
		canvas = new Canvas();
		canvas.setBackground(appFondo);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setLocationByPlatform(true);
		if (appMantenerRatio) {
			getContentPane().setBackground(appMargen);
			setSize(appAncho, appAlto);
			canvas.setSize(appAncho, appAlto);
			setLayout(null);
			getContentPane().addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					onComponentResized(e);
				}
			});
		} else {
			canvas.setSize(appAncho, appAlto);
			pack();
		}
		setTitle(appTitulo);
		teclado = new InputTeclado();
		canvas.addKeyListener(teclado);
		mouse = new InputMouse(canvas);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);
		setVisible(true);
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		canvas.requestFocus();
		gameThread = new Thread(this);
		gameThread.start();
	}

	protected void onComponentResized(ComponentEvent e) {
		Dimension size = getContentPane().getSize();
		int vw = (int) (size.width * appMargenEscala);
		int vh = (int) (size.height * appMargenEscala);
		int vx = (size.width - vw) / 2;
		int vy = (size.height - vh) / 2;
		int newW = vw;
		int newH = (int) (vw * appMundoAlto / appMundoAncho);
		if (newH > vh) {
			newW = (int) (vh * appMundoAncho / appMundoAlto);
			newH = vh;
		}
		// center
		vx += (vw - newW) / 2;
		vy += (vh - newH) / 2;
		canvas.setLocation(vx, vy);
		canvas.setSize(newW, newH);
	}

	protected Matrices getViewportTransform() {
		return Utilidad.createViewport(appMundoAncho, appMundoAlto,
				canvas.getWidth(), canvas.getHeight());
	}

	protected Matrices getReverseViewportTransform() {
		return Utilidad.createReverseViewport(appMundoAncho, appMundoAlto,
				canvas.getWidth(), canvas.getHeight());
	}

	protected Vectores getWorldMousePosition() {
		Matrices screenToWorld = getReverseViewportTransform();
		Point mousePos = mouse.getPosition();
		Vectores screenPos = new Vectores(mousePos.x, mousePos.y);
		return screenToWorld.mul(screenPos);
	}

	protected Vectores getRelativeWorldMousePosition() {
		float sx = appMundoAncho / (canvas.getWidth() - 1);
		float sy = appMundoAlto / (canvas.getHeight() - 1);
		Matrices viewport = Matrices.scale(sx, -sy);
		Point p = mouse.getPosition();
		return viewport.mul(new Vectores(p.x, p.y));
	}

	public void run() {
		running = true;
		initialize();
		long curTime = System.nanoTime();
		long lastTime = curTime;
		double nsPerFrame;
		while (running) {
			curTime = System.nanoTime();
			nsPerFrame = curTime - lastTime;
			gameLoop((float) (nsPerFrame / 1.0E9));
			lastTime = curTime;
		}
		terminate();
	}

	protected void initialize() {
		frameRate = new FrameRate();
		frameRate.initialize();
	}

	protected void terminate() {
	}

	private void gameLoop(float delta) {
		processInput(delta);
		updateObjects(delta);
		renderFrame();
		sleep(appSleep);
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

	protected void processInput(float delta) {
		teclado.poll();
		mouse.poll();
	}

	protected void updateObjects(float delta) {
	}

	protected void render(Graphics g) {
		g.setFont(appFont);
		g.setColor(appFPSColor);
		frameRate.calculate();
		textPos = Utilidad.drawString( 
			g, 20, 0, frameRate.getFrameRate() 
		); //new
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

	protected static void launchApp(final Framework app) {
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
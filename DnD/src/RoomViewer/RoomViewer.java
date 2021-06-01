package RoomViewer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;

import RoomViewer.StateManager;
import Misc.KeyManager;
import Misc.MouseManager;

public class RoomViewer extends Thread {
	boolean running = true;
	public boolean unfocusedRendering = true, unfocusedUpdating = true;
	public boolean alwaysUpdate = true;
	public int update = 120;
	public int width = 500, height = 500, devMode = 1, maxFPS = 120;
	public double fps = 0;
	public ArrayList<String> debugMessages = new ArrayList<String>();

	public JFrame frame;
	private Canvas canvas;
	public KeyManager keyManager;
	public MouseManager mouseManager;
	public Thread load;
	public String name = "MISSING WINDOW NAME";
	StateManager stateManager = new StateManager();

	public static void main(String args[]) {
		new RoomViewer().start();
	}
	
	public RoomViewer() {
		fps = maxFPS;
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
	}

	public void init() {
		frame = new JFrame(name);
		// frame.setUndecorated(true);
		frame.setSize(width, height);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				running = false;
			}
		});
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addMouseListener(mouseManager);
		frame.addMouseMotionListener(mouseManager);
		frame.addMouseWheelListener(mouseManager);

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);
		canvas.addMouseListener(mouseManager);
		canvas.addMouseMotionListener(mouseManager);
		canvas.addMouseWheelListener(mouseManager);
		frame.add(canvas);
		frame.pack();

		frame.addKeyListener(keyManager);

		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		width = canvas.getWidth();
		height = canvas.getHeight();
	}

	private void tick() {
		long startTime = System.nanoTime();
		width = canvas.getWidth();
		height = canvas.getHeight();
		keyManager.tick();

		if (KeyManager.keyRelease(KeyEvent.VK_EQUALS) & devMode < 1)
			devMode++;
		if (KeyManager.keyRelease(KeyEvent.VK_MINUS) & devMode > 0)
			devMode--;
		
		stateManager.tick();
		
		tickTime = System.nanoTime() - startTime;
	}

	private BufferStrategy bs;
	private Graphics g;

	private void render() {
		if (update <= 0 & !alwaysUpdate)
			return;
		if (update > 0)
			update -= 1;

		bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();

		// Clear Screen
		g.clearRect(0, 0, width, height);
		g.setColor(new Color(18, 20, 21));
		g.fillRect(0, 0, width, height);
		// Draw Here!

		Rendering.Graphics gg = new Rendering.Graphics((Graphics2D) g);
		gg.setDim(width, height);
		
		stateManager.render(gg);
		
		// Drawing fps

		if (devMode > 0) {
			g.setColor(Color.green);
			g.setFont(new Font("Serif", Font.PLAIN, 15));
			debugMessages.add("FPS " + fps);
			debugMessages.add("Tick Time: " + tickTime / 1_000_000.0 + "ms");
			debugMessages.add("Render Time: " + renderTime / 1_000_000.0 + "ms");
			for (int x = 0; x < debugMessages.size(); x++) {
				g.drawString(debugMessages.get(x), 0, (x + 1) * 15);
			}
			g.drawString("Dev Mode " + devMode, 0, height - 20);
		}
		// End Drawing!
		bs.show();
		g.dispose();
		debugMessages = new ArrayList<String>();
	}

	static long tickTime = 0, renderTime = 0;

	public void run() {
		init();
		// Tick
		boolean threadTick = true;
		Thread tickThread;
		Thread renderThread;
		if (threadTick) {
			tickThread = new Thread(new Runnable() {
				public void run() {
					int ticks = 0;
					double timePerTick = 1000000000 / maxFPS;
					double delta = 0;
					long now;
					long lastTime = System.nanoTime();
					long timer = 0;
					while (running) {
						if (!unfocusedUpdating & !frame.hasFocus()) {
							System.out.println("skip");
							continue;
						}
						// System.out.println("Tick");
						now = System.nanoTime();
						delta += (now - lastTime) / timePerTick;
						timer += now - lastTime;
						lastTime = now;
						if (delta >= 1) {
							tick();
							ticks++;
							delta--;
						}
						if (timer >= 1000000000) {
							// fps=ticks;
							ticks = 0;
							timer = 0;
						}
					}
					// System.out.println("Tick Stop");
				}
			});
		} else {
			tickThread = new Thread(new Runnable() {
				public void run() {
					while (running) {
						if (!unfocusedUpdating & !frame.hasFocus())
							continue;
						tick();
					}
				}
			});
		}
		renderThread = new Thread(new Runnable() {
			public void run() {
				long frames = 0;
				long lastTime = System.nanoTime();
				while (running) {
					// Skip render if
					if (!unfocusedRendering & !frame.hasFocus())
						continue;
					long startTime = System.nanoTime();
					render();
					frames++;
					if ((System.nanoTime() - lastTime) / 1_000_000_000 >= 1) {
						fps = frames;
						frames = 0;
						lastTime = System.nanoTime();
					}
					renderTime = System.nanoTime() - startTime;
				}
			}
		});
		tickThread.start();
		renderThread.start();

		while (tickThread.isAlive() & renderThread.isAlive()) {
		}
		// System.out.println("End");
		System.exit(0);
	}
}

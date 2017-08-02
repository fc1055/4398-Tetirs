import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainActivity extends JPanel implements Runnable{
	
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	public JFrame window;
	private Board board;
	public static final double ns = 1000000000.0 / 60.0;	// nanoseconds conversion 

	
	public MainActivity(){

		window = new JFrame("Tetris Game");
		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		
		board = new Board();
		window.getContentPane().add(board);
		window.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					board.rotate();
					break;
				case KeyEvent.VK_DOWN:
					board.speedDown();
					break;
				case KeyEvent.VK_LEFT:
					board.moveRight();
					break;
				case KeyEvent.VK_RIGHT:
					board.moveLeft();
					break;
				case KeyEvent.VK_SPACE:
					board.drop();
					break;
				} 
			}
			
			public void keyReleased(KeyEvent e) {
			}
		});
		
		
		window.setVisible(true);
		
	}
	
	private Thread thread;
	private boolean running = false;
	
	/**
	 * start() - creates a game thread and calls run() method 
	*/
	private synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void stop() {
		if (!running)
			return;
		
		running = false;
		try 
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		//System.exit(1);
	}
	/**
	 * run() - game loop updates moving block every second and renders 
	 * graphics every iteration
	*/
	public void run() {
		// timer variables
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		//final double ns = 1000000000.0 / 60.0;	// nanoseconds conversion 
		double delta = 0;						// elapsed time b/w now and lastTime
		// fps counter variables
		int frames = 0;							// counts how many frames we have time to render
		int updates = 0;						// counts how many times update() is called
		
		while (running) {
			long now = System.nanoTime();
			delta += (now-lastTime) / ns;		// adds a 60th of a sec each iteration
			lastTime = now;
			while (delta >= 1 ) {				// update 60 times per second (should it be an if?)
				update();
				updates++;
				delta--;
			}
			frames++;
			
			// FPS counter
			if (System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				//System.out.println(updates + " updates" + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public void update() {
		board.updatePosition();
		board.repaint();
	}
	

	
	public static void main(String[] args) {
		MainActivity game = new MainActivity();
    
        game.start();
	}
	
}

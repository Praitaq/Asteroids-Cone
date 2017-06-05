import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.Random;



public class Asteroids extends Applet implements Runnable, KeyListener {

	// Main thread(game loop)
	Thread gameloop;
	
	// double buffer - allows for smoother animation
	BufferedImage backbuffer;
	
	// graphics context to draw to back buffer
	Graphics2D g2d;
	
	// toggle for drawing bounding boxes
	boolean drawBounds = false;
	
	// Create the Asteroid array
	int ASTEROIDS = 20;
	Asteroid[] ast = new Asteroid[ASTEROIDS];
	
	// create the bullet array
	int BULLETS = 10;
	Bullet[] bullet = new Bullet[BULLETS];
	int currentBullet = 0;
	
	// Player's Ship
	Ship ship = new Ship();
	
	// create the Transform Identity
	AffineTransform identity = new AffineTransform();
	
	// Init the RNG
	Random rand = new Random();
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	/**************************************************************************************************/
	
	// applet init event
	public void init() {
		// create the back buffer
		this.backbuffer = new BufferedImage(640,480,BufferedImage.TYPE_INT_RGB);
		this.g2d = backbuffer.createGraphics();
		this.setSize(640, 480);
		// setup the player ship
		this.ship.setX(320);
		this.ship.setY(240);
		
		// init the bullets
		for(int i = 0; i < BULLETS; i++) {
			this.bullet[i] = new Bullet();
		}
		
		// init the asteroids
		for(int i = 0; i < ASTEROIDS; i++) {
			ast[i] = new Asteroid();
			ast[i].setRotationVelocity(rand.nextInt(3) + 1);
			ast[i].setX((double)rand.nextInt(600)+ 20);
			ast[i].setY((double)rand.nextInt(440)+ 20);
			ast[i].setMoveAngle(rand.nextInt(360));
			double ang = ast[i].getMoveAngle() - 90;
			ast[i].setVelX(calcAngleMoveX(ang));
			ast[i].setVelY(calcAngleMoveY(ang));
		}
		
		// add the key listener
		addKeyListener(this);
	}
	
	// applet update event
	public void update(Graphics g) {
		
		// start transforms at the Identity
		g2d.setTransform(identity);
		
		// clear the background
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, getSize().width, getSize().height);
		
		// Print status info( info bar )
		g2d.setColor(Color.WHITE);
		g2d.drawString("Ship: " + Math.round(ship.getX()) + ",  " + Math.round(ship.getY()) , 5,10);
		g2d.drawString("Move Angle: " + Math.round(ship.getMoveAngle()) + 90 , 5,25);
		g2d.drawString("Face Angle: " + Math.round(ship.getFaceAngle()) , 5,40);
		g2d.drawString("Current Bullet: " + currentBullet , 5,55);
		
		// Draw the game objects
		drawShip();
		drawBullets();
		drawAsteroids();
		
		
		// finally repain the window
		paint(g);
	}
	
	// applet redraw event
	public void paint(Graphics g) {
		// draw the backbuffer onto the screen
		g.drawImage(backbuffer, 0, 0, this);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	/**************************************************************************************************/
	
	private void drawAsteroids() {
		// step through the asteroid array
				for(int i =0; i < ASTEROIDS; i ++) {
					
					// check allive
					if(ast[i].isAlive()) {
						// draw teh bullet
						g2d.setTransform(identity);
						g2d.translate(ast[i].getX(), ast[i].getY());
						g2d.rotate(Math.toRadians(ast[i].getMoveAngle()));
						g2d.setColor(Color.DARK_GRAY);
						g2d.fill(ast[i].getShape());
					}
				}
	}

	private void drawBullets() {
		// step through the bullet array
		for(int i =0; i < BULLETS; i ++) {
			
			// check allive
			if(bullet[i].isAlive()) {
				// draw teh bullet
				g2d.setTransform(identity);
				g2d.translate(bullet[i].getX(), bullet[i].getY());
				g2d.setColor(Color.MAGENTA);
				g2d.fill(bullet[i].getShape());
			}
		}
		
	}

	private void drawShip() {
		// Draws the ship to the screen
		g2d.setTransform(identity);
		g2d.translate(ship.getX(), ship.getY());
		g2d.rotate(Math.toRadians(ship.getFaceAngle()));
		g2d.setColor(Color.ORANGE);
		g2d.fill(ship.getShape());
			
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	/**************************************************************************************************/

	// thread start event - starts the game loop
	public void start() {
		gameloop = new Thread(this);
		gameloop.start();
	}

	// thread run event - main game logic here
	public void run() {
		// acquire the current thread
		Thread t = Thread.currentThread();
		
		// keep going as long as the thread is alive
		while(t== gameloop) {
			try {
				// update the game
				gameUpdate();
				
				//tgt framerate is 5-fts
				Thread.sleep(20);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			repaint();
		}

	}


	// thread stop event - kill the game
	public void stop() {
		gameloop = null;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	/**************************************************************************************************/

	private void gameUpdate() {
		updateShip();
		updateBullets();
		updateAsteroids();
		checkCollisions();
	}


	public void checkCollisions() {
		// Tests for and performs all ingame collisions
		
		for(int i = 0; i < ASTEROIDS; i++) {
			
			// is it alive?
			if(ast[i].isAlive()) {
				//check for bullet collisions
				for(int b =0; b < BULLETS;b++) {
					if(bullet[b].isAlive()) {
						if(ast[i].getBounds().contains(bullet[b].getX(), bullet[b].getY())) {
							// bullet is within the bounding box of hte asteroid
							bullet[b].setAlive(false);
							ast[i].setAlive(false);
							continue;
						}
					}
				}
				// check for ship to asteroid collisions
				if(ast[i].getBounds().intersects(ship.getBounds())) {
					ast[i].setAlive(false);
					ship.setX(320);
					ship.setY(240);
					ship.setFaceAngle(0);;
					ship.setVelX(0);
					ship.setVelY(0);
					ship.setAlive(true);
					continue;
			}
			
			
			}
		}
		
		
	}

	public void updateAsteroids() {
		// performs all the logic associated with updating the asteroids on the play field
		
		for(int i = 0; i < ASTEROIDS; i++) {
			// is the asteroid alive?
			if(ast[i].isAlive()) {
				// update teh asteroids x val
				ast[i].incX(ast[i].getVelX());
				
				// wrap edges
				if(ast[i].getX() < -20) ast[i].setX(getSize().width + 20);
				else if(ast[i].getX() > getSize().width+20) ast[i].setX(-20);
				
				// update asteroids Y val
				ast[i].incY(ast[i].getVelY());
				
				// wrap edges
				if(ast[i].getY() < -20) ast[i].setY(getSize().height + 20);
				else if(ast[i].getY() > getSize().height+20) ast[i].setY(-20);
				
				// update asteroids rotation
				ast[i].incMoveAngle(ast[i].getRotationVelocity());
				
				// keep rotation with in 0-359 deg
				if(ast[i].getMoveAngle() < 0 ) ast[i].setMoveAngle(360-ast[i].getRotationVelocity());
				else if(ast[i].getMoveAngle() > 359) ast[i].setMoveAngle(ast[i].getRotationVelocity());
				
				
			}
		}
		
	}

	public void updateBullets() {
		// performs all the logic assicaiated with updating the bullet objects
		
		for(int i =0; i < BULLETS; i++) {
			// if alive
			if(bullet[i].isAlive()) {
				
				// update the X pos
				bullet[i].incX(bullet[i].getVelX());
				
				// if it crosses the screen edge, kill it!
				if((bullet[i].getX() < 0)||(bullet[i].getX() > getSize().width)) bullet[i].setAlive(false);
				
				// update the X pos
				bullet[i].incY(bullet[i].getVelY());
				
				// if it crosses the screen edge, kill it!
				if((bullet[i].getY() < 0)||(bullet[i].getY() > getSize().height)) bullet[i].setAlive(false);
			}
		}
		
	}

	public void updateShip() {
		// performs all the logic associated with updating the player ship object
		ship.incX(ship.getVelX());	// update X pos
		
		// wrap ship around screen
		if(ship.getX() < -10) ship.setX(getSize().width + 10);
		else if(ship.getX() > getSize().width + 10) ship.setX(-10);
		
		ship.incY(ship.getVelY());	// update Y pos
		
		// wrap ship around screen
		if(ship.getY() < -10) ship.setY(getSize().height + 10);
		else if(ship.getY() > getSize().height + 10) ship.setY(-10);
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	/**************************************************************************************************/

	// KeyReleased and KeyTyped events must implemented for the listener, but are not used
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	public void keyPressed(KeyEvent e) {
		// Handles all play input
		
		int keyCode = e.getKeyCode();		// retrieve the key pressed
		
		switch(keyCode) {
		
		case KeyEvent.VK_LEFT:
			// Turn the ship left
			ship.incFaceAngle(-5);
			if(ship.getFaceAngle() < 0) ship.setFaceAngle(360-5);
			break;
		case KeyEvent.VK_RIGHT:
			// turn the ship right
			ship.incFaceAngle(5);
			if(ship.getFaceAngle() >360) ship.setFaceAngle(5);
			break;
		case KeyEvent.VK_UP:
			// thrust forward
			ship.setMoveAngle(ship.getFaceAngle()-90);
			ship.incVelX(calcAngleMoveX(ship.getMoveAngle()) *0.1);
			ship.incVelY(calcAngleMoveY(ship.getMoveAngle()) *0.1);
			//System.out.println("dvX: " + ship.getVelX() + "\ndvY: " + ship.getVelY());
			break;
			
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_CONTROL:
		case KeyEvent.VK_ENTER:
			// Fire teh lazors
			currentBullet++;
			if(currentBullet > (BULLETS-1)) currentBullet = 0;
			bullet[currentBullet].setAlive(true);
			
			bullet[currentBullet].setX(ship.getX());
			bullet[currentBullet].setY(ship.getY());
			bullet[currentBullet].setMoveAngle(ship.getFaceAngle()-90);
			
			double angle = bullet[currentBullet].getMoveAngle();
			double svx = ship.getVelX();
			double svy = ship.getVelY();
			double camx = calcAngleMoveX(angle);
			double camy = calcAngleMoveY(angle);
			bullet[currentBullet].setVelX(svx+camx * 2);
			bullet[currentBullet].setVelY(svy+ camy * 2);
			System.out.println("Bullet Fired(svx=" + svx + "; svy="+svy+"; angle=" +angle +"; camx=" + camx+"; camy=" + camy + ')');
			break;	
		}
		

	}

	private double calcAngleMoveX(double angle) {
		return (double) (Math.cos(angle*Math.PI/180));
	}

	private double calcAngleMoveY(double angle) {
		return (double) (Math.sin(angle*Math.PI/180));
	}

	// helper methods
	


}

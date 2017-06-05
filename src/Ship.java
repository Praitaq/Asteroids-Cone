import java.awt.Polygon;
import java.awt.Rectangle;

public class Ship extends BasicVectorShape{
	// Define the shape of the ship
	private int shipX[] = {-6,-3,0,3,6,0};
	private int shipY[] = {6,7,7,7,6,-7};
	
	// bounding rectangle define
	public Rectangle getBounds() {
		Rectangle r = new Rectangle((int)getX()-6,(int) getY()-6,12,12);
		return r;
	}
	
	Ship() {
		setShape(new Polygon(shipX, shipY, shipX.length));	// Set the ships shape
		setAlive(true);										// Set ship status to alive
	}
}

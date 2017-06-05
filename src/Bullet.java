import java.awt.*;
import java.awt.Rectangle;

// Its a polygon Bullet! 1x1 
public class Bullet extends BasicVectorShape {
	
	public Rectangle getBounds() {
		Rectangle r = new Rectangle((int) getX(), (int) getY(), 1,1);
		return r;
	}
	
	Bullet() {
		setShape(new Rectangle(0,0,1,1));
		setAlive(true);
	}
}

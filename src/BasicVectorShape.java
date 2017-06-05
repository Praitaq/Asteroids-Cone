import java.awt.Shape;

public class BasicVectorShape {

	// Internals
	private Shape shape;
	private boolean alive;
	
	private double x, y;					// Position
	private double VelX, VelY;				// Velocity
	private double moveAngle, faceAngle;	// Angle it faces and angle it moves
	
	// Accessor Methods
	public Shape getShape() { return shape; };
	public boolean isAlive() { return alive; };
	
	public double getX() { return x; };
	public double getY() { return y; };
	public double getVelX() { return VelX; };
	public double getVelY() { return VelY; };
	public double getMoveAngle() { return moveAngle; };
	public double getFaceAngle() { return faceAngle; };
		
	// Mutator and Helper methods
	public void setShape(Shape s) { shape = s; };
	public void setAlive(boolean b) { alive = b; };
	
	public void setX(double X) { x = X; };
	public void incX(double X) { x += X; };
	public void setY(double Y) { y = Y; };
	public void incY(double Y) { y += Y; };
	public void setVelX(double velX) { VelX = velX; };
	public void incVelX(double velX) { VelX += velX; };
	public void setVelY(double velY) { VelY = velY; };
	public void incVelY(double velY) { VelY += velY; };
	public void setMoveAngle(double moveangle) { moveAngle = moveangle; };
	public void incMoveAngle(double moveangle) { moveAngle += moveangle; };
	public void setFaceAngle(double faceangle) { faceAngle = faceangle; };
	public void incFaceAngle(double faceangle) { faceAngle += faceangle; };

	// default constructor
	BasicVectorShape() {
		setShape(null);
		setAlive(false);
		setX(0.0);
		setY(0.0);
		setVelX(0.0);
		setVelY(0.0);
		setMoveAngle(0.0);
		setFaceAngle(0.0);
	}
	
}

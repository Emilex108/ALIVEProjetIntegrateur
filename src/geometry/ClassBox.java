package geometry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class ClassBox implements Drawable{
	
	private Rectangle2D rectangle;
	private int width;
	private int height;
	private int posX;
	private int posY;
	private BoxType boxType;
	
	
	public ClassBox(int width, int height, int posX, int posY, BoxType boxType) {
		this.width = width;
		this.height = height;
		this.posX = posX;
		this.posY = posY;
		this.boxType = boxType;
		rectangle = new Rectangle(posX, posY, width, height);
	}
	
	/**
	 * 
	 * @return the position of the top right corner
	 */
	public int getPosX() {
		return posX;
	}
	
	/**
	 * 
	 * @return the position of the top of the box
	 */
	public int getPosY() {
		return posY;
	}
	
	/**
	 * 
	 * @return the width of the box
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 
	 * @return the height of the box
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 
	 * @return the position of the center of the box in x
	 */
	public int getCenterX() {
		return posX+width/2;
	}
	
	/**
	 * 
	 * @return the position of the center of the box in y
	 */
	public int getCenterY() {
		return posY+height/2;
	}
	
	/**
	 * To draw the box and their class on the image
	 */
	public void dessiner(Graphics2D g2d){
		Color colorTemp = g2d.getColor();
		AffineTransform atr = g2d.getTransform();
		g2d.setColor(boxType.getColor());
		g2d.draw(rectangle);
		g2d.scale(2,2);
		g2d.drawString(boxType.getName(), posX/2, posY/2);
		g2d.setColor(colorTemp);
		g2d.setTransform(atr);
	}
}


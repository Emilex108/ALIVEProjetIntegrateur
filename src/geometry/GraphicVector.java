package geometry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;


public class GraphicVector extends Vector implements Drawable {
	
	private double origX=0, origY=0;		
	private Line2D.Double lineHead;
	private double angleHead = 0.5;
	private double lengthHead = 20;
	private String labelTag = "";
	private double vectorLength;
	private double vectorAngle;
	private final double MOVE_LABEL = 0.1;
	private Color color = Color.black;

	
	public GraphicVector() {
		super();
	}
	
	public GraphicVector (double origX, double origY) {
		this.origX = origX;
		this.origY = origY;
		vectorLength = 100;
		vectorAngle = 0;
	}
	public GraphicVector (double origX, double origY, double angle) {
		this.origX = origX;
		this.origY = origY;
		this.vectorLength = 50;
		this.vectorAngle = angle;
	}
	public GraphicVector (double origX, double origY, double length, double angle) {
		this.origX = origX;
		this.origY = origY;
		this.vectorLength = length;
		this.vectorAngle = angle;
	}
	private void creerLabel(Graphics2D g2d) {
		g2d.translate(origX + x *(1 + MOVE_LABEL), origY + y * (1 + MOVE_LABEL));
		g2d.drawString(labelTag, 0, 0);
	}
	
	/**
	 * Dessine le vecteur sous la forme d'une flèche orientée
	 * @param g2d Le contexte graphique
	 */
	public void dessiner(Graphics2D g2d) {	
		Color colorStock = g2d.getColor();
		g2d.setColor(color);
		lineHead = new Line2D.Double(vectorLength -10,0,vectorLength, 0);
		AffineTransform mat = g2d.getTransform();
		g2d.translate(origX, origY);
		Line2D.Double body = new Line2D.Double(0,0,vectorLength, 0);
		g2d.rotate(Math.toRadians(vectorAngle));
		g2d.draw(body);
		g2d.rotate(angleHead/2, vectorLength, 0);
		g2d.draw(lineHead);
		g2d.rotate(-angleHead, vectorLength, 0);
		g2d.draw(lineHead);
		g2d.setTransform(mat);
		
		creerLabel(g2d);
		g2d.setTransform(mat);
		g2d.setColor(colorStock);
	}
	
	/**
	 * Modifie l'origine du vecteur pour son dessin
	 * @param origX origine en x
	 * @param origY origine en y
	 */
	public void setOrigineXY(double origX, double origY) {
		this.origX = origX;
		this.origY = origY;
	}
	
	/**
	 * Retourne la longueur du segment utilisé pour tracer la flèche formant l'extrémité du vecteur
	 * @return Longueur du segment
	 */
	public double getLongueurTete() {
		return lengthHead;
	}

	/**
	 * Modifie la longueur du segment utilisé pour tracer la flèche formant l'extrémité du vecteur
	 * @param longueurTete longueur du segment
	 */
	public void setLongueurTete(double longueurTete) {
		this.lengthHead = longueurTete;
	}

	/**
	 * Sets the head angle
	 * @param angleHead L'angle de la tête
	 */
	public void setAngleTete(double angleHead) {
		this.angleHead = angleHead;
	}

	/**
	 * @param label The label to set
	 */
	public void setLabel(String label) {
		this.labelTag = label;
	}
	/**
	 * @param length The length to set
	 */
	public void setLength(double length) {
		if(length>=8) {
			this.vectorLength=8;
			setColor(Color.black);
		}else {
			this.vectorLength = length;
			setColor(Color.RED);
		}
		
	}
	public void setAngle(double angle) {
		this.vectorAngle = angle;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	

}

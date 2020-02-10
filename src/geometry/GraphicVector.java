package geometry;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class GraphicVector extends Vector implements Drawable {
	
	//caractéristiques supplemetaires utiles pour le dessin
	private double origX=0, origY=0;			 //originep our dessiner le vecteur
	private Line2D.Double traitDeTete;    //pour tracer la flèche
	private double angleTete = 0.5;              //angle entre les deux segments formant la tete de fleche
	private double longueurTete = 20;            //longueur des segments formant la tete (en pixels)
	private String label = "";
	private double length;
	private double angle;
	private final double LABEL_DEPLACEMENT = 0.1;

	
	public GraphicVector() {
		super();
	}
	
	public GraphicVector (double origX, double origY) {
		this.origX = origX;
		this.origY = origY;
		length = 100;
		angle = 0;
	}
	public GraphicVector (double origX, double origY, double angle) {
		this.origX = origX;
		this.origY = origY;
		this.length = 50;
		this.angle = angle;
	}
	public GraphicVector (double origX, double origY, double length, double angle) {
		this.origX = origX;
		this.origY = origY;
		this.length = length;
		this.angle = angle;
	}
	private void creerLabel(Graphics2D g2d) {
		System.out.println(label + " " + x + "/" + y);
		g2d.translate(origX + x *(1 + LABEL_DEPLACEMENT), origY + y * (1 + LABEL_DEPLACEMENT));
		g2d.drawString(label, 0, 0);
	}
	
	/**
	 * Dessine le vecteur sous la forme d'une flèche orientée
	 * @param g2d Le contexte graphique
	 */
	@Override
	public void dessiner(Graphics2D g2d) {	
		traitDeTete = new Line2D.Double(length -10,0,length, 0);
		AffineTransform mat = g2d.getTransform();
		g2d.translate(origX, origY);
		System.out.println("origX = "+origX + "\norigY = "+origY );
		Line2D.Double body = new Line2D.Double(0,0,length, 0);
		g2d.rotate(Math.toRadians(angle));
		g2d.draw(body);  										//ligne formant le vecteur lui-meme
		g2d.rotate(angleTete/2, length, 0);
		g2d.draw(traitDeTete); 										//un des deux traits qui forment la tete du vecteur
		g2d.rotate(-angleTete, length, 0);
		g2d.draw(traitDeTete);
		g2d.setTransform(mat);
		
		creerLabel(g2d);
		g2d.setTransform(mat);
	}// fin
	
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
		return longueurTete;
	}

	/**
	 * Modifie la longueur du segment utilisé pour tracer la flèche formant l'extrémité du vecteur
	 * @param longueurTete longueur du segment
	 */
	public void setLongueurTete(double longueurTete) {
		this.longueurTete = longueurTete;
	}

	/**
	 * Mets l'angle de la tête
	 * @param angleTete L'angle de la tête
	 */
	public void setAngleTete(double angleTete) {
		this.angleTete = angleTete;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLength(double length) {
		this.length = length;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	

}

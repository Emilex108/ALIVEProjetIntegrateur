package geometry;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class GraphicVector extends Vector implements Drawable {
	
	//caract�ristiques supplemetaires utiles pour le dessin
	private double origX=0, origY=0;			 //originep our dessiner le vecteur
	private Line2D.Double traitDeTete;    //pour tracer la fl�che
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
	}
	
	private void creerLabel(Graphics2D g2d) {
		System.out.println(label + " " + x + "/" + y);
		g2d.translate(origX + x *(1 + LABEL_DEPLACEMENT), origY + y * (1 + LABEL_DEPLACEMENT));
		g2d.drawString(label, 0, 0);
	}
	
	/**
	 * Dessine le vecteur sous la forme d'une fl�che orient�e
	 * @param g2d Le contexte graphique
	 */
	@Override
	public void dessiner(Graphics2D g2d) {	
		AffineTransform mat = g2d.getTransform();
		g2d.translate(origX, origY);
		
		//g2d.draw(  );  										//ligne formant le vecteur lui-meme
		g2d.rotate(angleTete/2, x,  y);
		g2d.draw(traitDeTete); 										//un des deux traits qui forment la tete du vecteur
		g2d.rotate(-angleTete, x, y);
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
	 * Retourne la longueur du segment utilis� pour tracer la fl�che formant l'extr�mit� du vecteur
	 * @return Longueur du segment
	 */
	public double getLongueurTete() {
		return longueurTete;
	}

	/**
	 * Modifie la longueur du segment utilis� pour tracer la fl�che formant l'extr�mit� du vecteur
	 * @param longueurTete longueur du segment
	 */
	public void setLongueurTete(double longueurTete) {
		this.longueurTete = longueurTete;
	}

	/**
	 * Mets l'angle de la t�te
	 * @param angleTete L'angle de la t�te
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

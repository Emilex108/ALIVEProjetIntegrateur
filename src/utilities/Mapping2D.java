package utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Mapping2D extends JFrame {

	private static final long serialVersionUID = -4652705085854314829L;

	private ArrayList<Ellipse2D.Double> points = new ArrayList<Ellipse2D.Double>();
	
	private boolean isRolling =true;
	private double posX=0;
	private double posY=0;
	
	//with a speed of 110
	private final double MAXSPEED = 0.4;
	
	private final double CAR_WIDTH = 0.20;
	private final int ABBERATE_DISTANCE = 30;
	private final double OBSTACLES_WIDTH = 0.10;
	
	private boolean firstTime =false;
	private DisplayModel display;
	
	private Ellipse2D car;
	
	public Mapping2D() {
		this.add(new DrawPanel());
		this.setPreferredSize(new Dimension(500,500));
		this.setSize(new Dimension(500,500));
		setTitle("Mapping2D");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void addPoint(double posX, double posY) {
		points.add(new Ellipse2D.Double(posX, posY, OBSTACLES_WIDTH,OBSTACLES_WIDTH));
		repaint();
	}
	
	public void trackPosition(int angle, double nTime) {
		if(isRolling) {
			double timeS = nTime*Math.pow(10, -9);
			double angleRad = Math.toRadians(angle);
			double distance = MAXSPEED*timeS;
			posX+= distance*Math.sin(angleRad);
			posY+= distance*Math.cos(angleRad);
			repaint();
			System.out.println("Position moved");
		}
		System.out.println("TrackedPosition false");
	}
	
	public void wallDetection(int distanceLeft, int distanceCenter, int distanceRight, int angle) {
		double posXWall = 0;
		double posYWall = 0;
		double angleRad = Math.toRadians(angle);
		if(distanceLeft<ABBERATE_DISTANCE) {
			posXWall = posX+distanceLeft/100.0*Math.sin(angleRad-Math.toRadians(60));
			posYWall = posY+distanceLeft/100.0*Math.cos(angleRad-Math.toRadians(60));
			addPoint(posXWall, posYWall);
		}
		if(distanceCenter<ABBERATE_DISTANCE) {
			posXWall = posX+distanceCenter/100.0*Math.sin(angleRad);
			posYWall = posY+distanceCenter/100.0*Math.cos(angleRad);
			addPoint(posXWall, posYWall);
		}
		if(distanceRight<ABBERATE_DISTANCE) {
			posXWall = posX+distanceCenter/100.0*Math.sin(angleRad+Math.toRadians(60));
			posYWall = posY+distanceCenter/100.0*Math.cos(angleRad+Math.toRadians(60));
			addPoint(posXWall, posYWall);
		}
		
	}
	
	public void setRolling(boolean isRolling) {
		this.isRolling = isRolling;
	}
	
	private class DrawPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 6353638320113149132L;

		protected DrawPanel() {
			this.setPreferredSize(new Dimension(500,500));
			this.setSize(new Dimension(500,500));
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			if(!firstTime) {
				display = new DisplayModel(getWidth(), getHeight(), 10);	
				firstTime=false;
			}
			
			AffineTransform mat = display.getModel();
		
			
			g2d.translate(this.getWidth()/2.0, this.getHeight()/2.0);
			
			car =  new Ellipse2D.Double(posX, posY, CAR_WIDTH, CAR_WIDTH);
			g2d.setColor(Color.red);
			g2d.fill(mat.createTransformedShape(car));
			
			g2d.setColor(Color.black);
			for(Ellipse2D e : points) {
				g2d.fill(mat.createTransformedShape(e));
			}
		}
		
	}
	
}


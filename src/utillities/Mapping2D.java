package utillities;

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

import aapplication.Application;

public class Mapping2D extends JFrame {

	private ArrayList<Ellipse2D.Double> points = new ArrayList<Ellipse2D.Double>();
	
	private boolean isRolling =true;
	private double posX=0;
	private double posY=0;
	//with a speed of 110
	private final double MAXSPEED = 0.4;
	private final double CAR_WIDTH = 0.20;
	private final int ABBERATE_DISTANCE = 200;
	private double lastAngle=0;
	
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
		points.add(new Ellipse2D.Double(posX, posY, 0.05,0.05));
		repaint();
	}
	
	public void trackPosition(int angle) {
		if(isRolling) {
			double timeS = Application.getAi().getDelay()*Math.pow(10, -9);
			double angleRad = Math.toRadians(angle);
			double distance = MAXSPEED*timeS;
			posX+= distance*Math.sin(angleRad);
			posY+= distance*Math.cos(angleRad);
			repaint();
		}
	}
	
	public void wallDetection(int distanceLeft, int ditanceCenter, int distanceRight, int angle) {
		double posX;
		double posY;
		double angleRad = Math.toRadians(angle);
		if(distanceLeft<ABBERATE_DISTANCE) {
			posX = distanceLeft/100*Math.sin(angleRad);
			posY = distanceLeft/100*Math.cos(angleRad);
		}
	}
	
	private class DrawPanel extends JPanel {
		
		protected DrawPanel() {
			this.setPreferredSize(new Dimension(500,500));
			this.setSize(new Dimension(500,500));
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			DisplayModel display = new DisplayModel(getWidth(), getHeight(), 10);	
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


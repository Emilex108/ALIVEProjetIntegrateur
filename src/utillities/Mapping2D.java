package utillities;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Mapping2D extends JFrame {

	private ArrayList<Ellipse2D.Double> points = new ArrayList<Ellipse2D.Double>();
	
	private boolean isRolling =true;
	private double posX=0;
	private double posY=0;
	//with a speed of 110
	private final double MAXSPEED = 0.4;
	private double lastAngle=0;
	
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
	
	public void trackPosition(String angle) {
		if(isRolling) {
			double timeS = delay*Math.pow(10, -9);
			double angleDeg = readingAngle(angle);
			System.out.println("AngleDeg : "+angleDeg);
			double angleRad = Math.toRadians(angleDeg);
			double distance = MAXSPEED*timeS;
			posX+= distance*Math.sin(angleRad);
			posY+= distance*Math.cos(angleRad);
			addPoint(posX, posY);
		}
	}
	
	public double readingAngle(String tangle) {
		int stringLenght = tangle.length();
		System.out.println("tangle: " +tangle);
		String convertAngle = "";
		if(tangle.length()%2 == 0) {
		for(int i = 0; i<stringLenght; i+=2) {
			convertAngle+= (Integer.parseInt(tangle.substring(i,i+2))-48)+"";
			}
		lastAngle = Double.parseDouble(convertAngle);
		return Double.parseDouble(convertAngle);
		}
		return lastAngle;
	}
	
	private class DrawPanel extends JPanel {
		
		protected DrawPanel() {
			this.setPreferredSize(new Dimension(500,500));
			this.setSize(new Dimension(500,500));
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			DisplayModel display = new DisplayModel(getWidth(), getHeight(), 3);	
			AffineTransform mat = display.getModel();
			g2d.translate(this.getWidth()/2.0, this.getHeight()/2.0);
			for(Ellipse2D e : points) {
				g2d.fill(mat.createTransformedShape(e));
			}
		}
		
	}
	
}


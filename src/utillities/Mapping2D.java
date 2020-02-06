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
	
	public Mapping2D() {
		this.add(new DrawPanel());
		this.setPreferredSize(new Dimension(500,500));
		this.setSize(new Dimension(500,500));
		setTitle("Mapping2D");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void addPoint(Point point) {
		points.add(new Ellipse2D.Double(point.x, point.y, 5,5));
		repaint();
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
			g2d.translate(this.getWidth()/2, this.getHeight()/2);
			for(Ellipse2D e : points) {
				g2d.fill(mat.createTransformedShape(e));
			}
		}
		
	}
	
}


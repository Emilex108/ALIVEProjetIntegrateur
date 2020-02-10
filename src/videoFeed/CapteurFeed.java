package videoFeed;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class CapteurFeed extends VideoFeed{
	
		private double distanceL;
		private double distanceR;
		private double distanceC;
		
		//private Arrow arrowL;
		//private Arrow arrowR;
		//private Arrow arrowC;
		
		private final double ARROWHEAD_LENGTH = 10;
		private final double HEAD_ANGLE = 45;
		
		
		/**
		 * Constructor with all the distances
		 * @param distanceL left captor's distance
		 * @param distanceR right captor's distance
		 * @param distanceC center captor's distance
		 */
		public CapteurFeed(double distanceL, double distanceR, double distanceC) {
			super();
			this.distanceL = distanceL;
			this.distanceR = distanceR;
			this.distanceC = distanceC;
		}
		
		/**
		 * Paint the arrows on the feed
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			
			AffineTransform atr = g2d.getTransform();
			
			AffineTransform atrArrow = new AffineTransform();
			
			//arrowL = new Arrow(false, distanceL, HEAD_ANGLE, ARROWHEAD_LENGTH);
			//arrowR = new Arrow(false, distanceR, HEAD_ANGLE, ARROWHEAD_LENGTH);
			//arrowC = new Arrow(false, distanceC, HEAD_ANGLE, ARROWHEAD_LENGTH);
			
			g2d.translate(getWidth()/2, getHeight());
			g2d.setTransform(atrArrow);
			
			//arrowC.dessiner(g2d);
			atrArrow.rotate(-HEAD_ANGLE);
			//arrowL.dessiner(g2d);
			atrArrow.rotate(2*HEAD_ANGLE);
			//arrowR.dessiner(g2d);
			
			
			g2d.translate(-getWidth()/2, -getHeight());
			g2d.setTransform(atr);
		}
}

package utillities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import geometry.GraphicVector;


public class DetectionPanel extends JPanel {

	private Image imgRead;
	private double width;
	GraphicVector gvA;
	GraphicVector gvG;
	GraphicVector gvD;
	
	public DetectionPanel() throws IOException {
		width = 100;
		gvG = new GraphicVector(140,135,-150);
		gvA = new GraphicVector(184, 125,-90);
		gvD = new GraphicVector(225,135,-30);
		imgRead = ImageIO.read(new File("resources/carUpView.png"));
		imgRead = imgRead.getScaledInstance((int) width,(int)(width * 1.3),Image.SCALE_SMOOTH);
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		System.out.println(imgRead.getWidth(null));
		g2d.drawImage(imgRead,getWidth()/2 - imgRead.getWidth(null)/2,125,(int) width,(int)(width * 1.3),null);
		gvA.dessiner(g2d);
		gvG.dessiner(g2d);
		gvD.dessiner(g2d);
	}

	public void setDistanceG(int G) {
		gvG.setLength(G);
		repaint();
	}

	public void setDistanceA(int A) {
		gvA.setLength(A);
		repaint();
	}

	public void setDistanceD(int D) {
		gvD.setLength(D);
		repaint();
	}

	
}

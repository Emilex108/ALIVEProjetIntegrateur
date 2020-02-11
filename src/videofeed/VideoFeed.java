package videofeed;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import geometry.BoxType;
import geometry.ClassBox;

public class VideoFeed extends JPanel implements Runnable {
	
	private BufferedImage imageNormale;
	private LinkedList<BufferedImage> listeImage = new LinkedList<BufferedImage>();
	private File dirFile= new File("C:\\Users\\e1833429\\Desktop\\dataset test");
	private File[] listeFichierImage;
	private String imagePath;
	private boolean animationEnCours = false;
	private long sleep;
	
	private final double FRAME_RATE=30;
	Thread thread;

	public VideoFeed() {
		loadingDirectory();	
	}
	
	/**
	 * Paint the JPanel
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		imageScaling(g2d);
		g2d.drawImage(imageNormale, 0, 0, null);
	}
	
	public void loadImage(String imagePath) {
		try {
			imageNormale = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/**
	 * Set the path of the image to show
	 * @param path the path from the root
	 */
	public void setImagePath(String path) {
		this.imagePath = path;
	}
	
	/**
	 * Calculate the scaling of the g2d so the image can fit in
	 * @param g2d object used to draw
	 */
	public void imageScaling(Graphics2D g2d) {
		double width = imageNormale.getWidth(null);
		double height = imageNormale.getHeight(null);
		double widthComposant = this.getWidth();
		double heightComposant = this.getHeight();
		
		double scaleX = widthComposant/width;
		double scaleY = heightComposant/height;
		
		g2d.scale(scaleX, scaleY);
	}
	/**
	 * Thread
	 */
	@Override
	public void run() {
		System.out.println("Depart du thread");
		while(animationEnCours) {
			System.out.println("Cest parti pour un tour de boucle while");
			framePreFrame();
			try {
				Thread.sleep((long) sleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Start the thread
	 */
	public void demarrer() {
		calculateSleep();
		if(animationEnCours==false) {
			thread = new Thread(this);
			thread.start();
		}
		
		animationEnCours = true;
	}
	
	/**
	 * stop the thread
	 */
	public void arreter() {
		animationEnCours = false;
	}
	
	/**
	 * Load the image directory file
	 */
	public void loadingDirectory() {
		listeFichierImage = dirFile.listFiles();
		if(dirFile != null) {
			for(File child : listeFichierImage) {
				loadImage(child.getPath());
				listeImage.add(imageNormale);
			}
		}
	}
	
	/**
	 * calculate the sleep time of the thread
	 */
	public void calculateSleep() {
		sleep = (long) ((1.0/FRAME_RATE)*1000.0);
	}
	
	/**
	 *Advance from one frame
	 */
	public void framePreFrame() {
		if(listeImage.peek()!=null) {
			imageNormale=listeImage.poll();
			listeImage.add(imageNormale);
		}
		repaint();
	}
	
}

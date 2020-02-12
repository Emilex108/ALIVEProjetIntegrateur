package videofeed;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import geometry.ClassBox;

public class BoxFeed extends VideoFeed{
	
	private ArrayList<ClassBox> boxList = new ArrayList<ClassBox>();
	
	/**
	 * Constructor for a VideoFeed with boxes on it
	 */
	public BoxFeed() {
		super();
	}
	
	/**
	 * Paint the image and the boxes above it
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		drawBoxes(g2d);
	}
	/**
	 * Add a box to the frame
	 * @param box the box to add to the frame
	 */
	public void addBox(ClassBox box) {
		boxList.add(box);
	}
	/**
	 * Clear the boxes from the frame
	 */
	public void emptyBoxList() {
		boxList.clear();
	}
	
	/**
	 * Goes trough the ArrayList and draw every box
	 * @param g2d used to draw
	 */
	public void drawBoxes(Graphics2D g2d) {
		for(int i = 0; i<boxList.size(); i++) {
			boxList.get(i).dessiner(g2d);
		}
	}
}

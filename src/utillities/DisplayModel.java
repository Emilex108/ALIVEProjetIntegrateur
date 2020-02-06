package utillities;

import java.awt.geom.AffineTransform;

public class DisplayModel {
	
	private double heightRealUnit = -1;
	private double widthRealUnit;
	private double widthPixels;
	private double heightPixel;
	private double pixelsPerUnitX;
	private double pixelsPerUnitY;
	private AffineTransform matRP;
	
	public DisplayModel(double widthPixels, double heightPixels, double widthRealUnit) {
		this.widthPixels = widthPixels;
		this.heightPixel = heightPixels;
		this.widthRealUnit = widthRealUnit;
		
		this.heightRealUnit = widthRealUnit * heightPixels/widthPixels;
		
		this.pixelsPerUnitX = widthPixels/widthRealUnit;
		this.pixelsPerUnitY = heightPixels/heightRealUnit;
		
		AffineTransform mat = new AffineTransform();
		mat.scale(pixelsPerUnitX, pixelsPerUnitY);
		
		this.matRP = mat;
	}
	
	public AffineTransform getModel() {
		return matRP;
		//huh
	}
}

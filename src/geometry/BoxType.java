package geometry;

import java.awt.Color;

public enum BoxType {
	TRAFFIC_lIGHT(Color.red, "Traffic light"),
	CAR(Color.blue, "Car"), 
	BIKE(Color.orange, "Bike"),
	TRAFFIC_SIGN(Color.yellow, "Traffic sign"),
	RIDER(Color.white, "Rider"),
	MOTOR(Color.blue, "Motor"),
	PERSON(new Color(8, 100, 65), "Person"), 
	TRUCK(new Color(132, 57, 169), "Truck");
	
	private Color color;
	private String name;
	
	BoxType(Color color, String name){
		this.color = color;
		this.name = name; 
	}
	/**
	 * 
	 * @return the color of the box
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * 
	 * @return the label of the box
	 */
	public String getName() {
		return name;
	}
}

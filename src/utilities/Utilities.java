package utilities;

/**
 * A class that contains all the methods that can be useful in multiples classes
 * @author Olivier St-Pierre
 *
 */
public class Utilities {

	
	public static int calculateAngle(int angle, int spin, int positive) {
		if(positive ==1) {
			return (angle+(spin*180))%360;
		}else {
			return -1*((angle+(spin*180))%360);
		}
	}
}

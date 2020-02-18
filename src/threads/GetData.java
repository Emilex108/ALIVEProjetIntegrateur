package threads;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.jsoup.Jsoup;

import aapplication.Application;
import listeners.DistanceChangedListener;
import utilities.Mapping2D;
import utilities.Utilities;
/**
 * This thread is created and used when the Autopilot is on, to update the data of the sensors on the Application
 * @author Émile Gagné
 */
public class GetData extends Thread{

	InputStream inStream;
	OutputStream outStream;
	int angleSign, angle, nbHalfTurn,a,d,g,isRolling;
	long startTime, endTime;
	Mapping2D map;
	private ArrayList<DistanceChangedListener> listenerList = new ArrayList<DistanceChangedListener>();;

	@Override
	public void run() {
		inStream = Application.getInStream();
		outStream = Application.getOutStream();
		System.out.println(inStream+","+outStream);
		map = new Mapping2D();
		while(true) {
			System.out.println(Application.getautoPilotOn());
			if(Application.getautoPilotOn() || Application.getAiOn()) {
				
				startTime = System.nanoTime();
				angleSign = getData(103);
				angle = getData(104);
				nbHalfTurn = getData(105);
				a = getData(100);
				g = getData(101);
				d = getData(102);
				isRolling = getData(106);
				System.out.println("test");
				if(isRolling == 1) {
					map.setRolling(true);
				}else {
					map.setRolling(false);
				}
				distanceChanged();
				endTime = System.nanoTime()-startTime;
				map.trackPosition(Utilities.calculateAngle(angle, nbHalfTurn, angleSign), endTime);
				map.wallDetection(g, a, d, Utilities.calculateAngle(angle, nbHalfTurn, angleSign));
			}
		}
	}
	/**
	 * Main method used to retrieve one answer from the Arduino
	 * @param command Number under 255 to identify a command to send to the Arduino
	 * @return Returns the number read (The answer)
	 */
	private int getData(int command){
		try {
			outStream.write(command);
			outStream.flush();
			while(inStream.available()==0);
			return Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * Adds a listener to the thread
	 * @param listener
	 */
	public void addDistanceChangedListener(DistanceChangedListener listener) {
		listenerList.add(listener);
	}
	public void distanceChanged() {
		for(DistanceChangedListener listener : listenerList) {
			listener.distanceChanged(g, a, d);
		}
	}
}

package threads;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.jsoup.Jsoup;

import aapplication.Application;
import listeners.DistanceChangedListener;
import utilities.Mapping2D;
import utilities.Utilities;

public class GetDistancesOnAutopilot extends Thread{

	InputStream inStream;
	OutputStream outStream;
	int angleSign, angle, nbHalfTurn,a,d,g,isRolling;
	long startTime, endTime;
	Mapping2D map;
	private ArrayList<DistanceChangedListener> listenerList = new ArrayList<DistanceChangedListener>();

	@Override
	public void run() {
		inStream = Application.getInStream();
		outStream = Application.getOutStream();
		map = new Mapping2D();
		while(Application.getautoPilotOn()) {
			try {
				startTime = System.nanoTime();
				outStream.write(103);
				outStream.flush();
				while(inStream.available()==0);
				angleSign = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
				System.out.println("Angle signe : " + angleSign);
				outStream.write(104);
				outStream.flush();
				while(inStream.available()==0);
				angle = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
				System.out.println("Angle : " + angle);
				outStream.write(105);
				outStream.flush();
				while(inStream.available()==0);
				nbHalfTurn = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
				System.out.println("Demi Tours : " + nbHalfTurn);
				outStream.write(100);
				outStream.flush();
				while(inStream.available()==0);
				a = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
				outStream.write(101);
				outStream.flush();
				while(inStream.available()==0);
				g = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
				outStream.write(102);
				outStream.flush();
				while(inStream.available()==0);
				d = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
				outStream.write(106);
				outStream.flush();
				while(inStream.available()==0);
				int isRolling = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
				if(isRolling == 1) {
					map.setRolling(true);
				}else {
					map.setRolling(false);
				}
				System.out.println("isRolling : " + isRolling);
				distanceChanged();
				endTime = System.nanoTime()-startTime;
				map.trackPosition(Utilities.calculateAngle(angle, nbHalfTurn, angleSign), endTime);
				System.out.println("TrackPosition call");
				map.wallDetection(g, a, d, Utilities.calculateAngle(angle, nbHalfTurn, angleSign));
			}catch(Exception e) {}
		}
	}

	public void addDistanceChangedListener(DistanceChangedListener listener) {
        listenerList.add(listener);
    }
    public void distanceChanged() {
        for(DistanceChangedListener listener : listenerList) {
            listener.distanceChanged(g, a, d);
        }
    }
}

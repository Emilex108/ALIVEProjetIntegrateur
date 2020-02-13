package threads;

import java.io.InputStream;
import java.io.OutputStream;

import org.jsoup.Jsoup;

import aapplication.Application;
import utilities.Mapping2D;
import utilities.Utilities;

public class GetDistancesOnAutopilot extends Thread{

	InputStream inStream;
	OutputStream outStream;
	int angleSign, angle, nbHalfTurn,a,d,g,isRolling;
	long startTime, endTime;
	Mapping2D map;

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
				int a = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
				outStream.write(101);
				outStream.flush();
				while(inStream.available()==0);
				int g = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
				outStream.write(102);
				outStream.flush();
				while(inStream.available()==0);
				int d = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
				outStream.write(106);
				outStream.flush();
				while(inStream.available()==0);
				int isRolling = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
				if(isRolling == 1) {
					map.setRolling = true;
				}else {
					map.setRolling = false;
				}
				endTime = System.nanoTime()-startTime;
				map.trackPosition(Utilities.calculateAngle(angle, nbHalfTurn, angleSign), endTime);
				map.wallDetection(g, a, d, Utilities.calculateAngle(angle, nbHalfTurn, angleSign));
			}catch(Exception e) {}
		}
	}

}

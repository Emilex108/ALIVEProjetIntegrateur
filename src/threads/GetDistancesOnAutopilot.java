package threads;

import java.io.InputStream;
import java.io.OutputStream;

import org.jsoup.Jsoup;

import aapplication.Application;
import utilities.Mapping2D;

public class GetDistancesOnAutopilot extends Thread{

	InputStream inStream;
	OutputStream outStream;
	int angleSign, angle, nbHalfTurn;
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
				endTime = System.nanoTime()-startTime;
				map.trackPosition(angle);
			}catch(Exception e) {}
		}
	}

}

package threads;

import java.io.InputStream;
import java.io.OutputStream;

import org.jsoup.Jsoup;

import aapplication.Application;

public class GetDistancesOnAutopilot extends Thread{

	InputStream inStream;
	OutputStream outStream;
	int angleSign, angle, nbHalfTurn;

	@Override
	public void run() {
		inStream = Application.getInStream();
		outStream = Application.getOutStream();
		while(Application.getautoPilotOn()) {
			try {
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
			}catch(Exception e) {}
		}
	}

	/**
	 * @return the angleSign
	 */
	public int getAngleSign() {
		return angleSign;
	}

	/**
	 * @return the angle
	 */
	public int getAngle() {
		return angle;
	}

	/**
	 * @return the nbHalfTurn
	 */
	public int getNbHalfTurn() {
		return nbHalfTurn;
	}

}

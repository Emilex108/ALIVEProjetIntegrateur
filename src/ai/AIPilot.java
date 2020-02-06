package ai;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.jsoup.Jsoup;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import aapplication.Application;
import utillities.Mapping2D;
/**
 * This class is used to load the trained AI and feed it the data gathered from the car sensors. 
 * It's sole purpose is to analyze that data and give an answer according to it.
 * @author Émile Gagné
 */
public class AIPilot extends Thread{

	private final double SCALER = 300.0;
	private MultiLayerNetwork MLN;
	private INDArray data, newData;
	private OutputStream outStream;
	private InputStream inStream;
	private int[] response;
	private Mapping2D map;
	
	private double angle;
	private double posX = 0;
	private double posY = 0;
	
	private boolean isRolling = false;
	//with a speed of 110
	private final double MAXSPEED = 0.4;

	public AIPilot(OutputStream outStream, InputStream inStream, MultiLayerNetwork MLN) throws IOException {
		this.MLN = MLN;
		this.outStream = outStream;
		this.inStream = inStream;
		map = new Mapping2D();
	}

	/**
	 * This method handles the decision making of the AI based on the data sensors
	 * @param d Distance on the right of the vehicle
	 * @param a Distance in front of the vehicle
	 * @param g Distance on the left of the vehicle
	 * @return Returns the index of the array with the highest percentage (Which represents the right decision)
	 */
	public int makeDecision(double d, double a, double g) {
		data = Nd4j.zeros(1,3);
		data.putScalar(0, d/SCALER);
		data.putScalar(1, a/SCALER);
		data.putScalar(2, g/SCALER);
		newData = MLN.output(data);
		double decision = 0;
		int decisionIndex = 3;
		for(int i = 0; i < newData.length(); i++) {
			if(newData.getDouble(i) > decision) {
				decision = newData.getDouble(i);
				decisionIndex = i;
			}
		}
		return decisionIndex;
	}
	/**
	 * This method handles receiving the data and sorting it in the right order
	 * @param inStream The InputStream of the program
	 * @param outStream The OutputStream of the program
	 * @return Returns a table of integers containing the distances of the sensors
	 * @throws NumberFormatException When the integer parsing fails
	 * @throws IOException When the I/O encounters a problem
	 * @throws InterruptedException When the sleep thread is interrupted
	 */
	public int[] receive(InputStream inStream, OutputStream outStream) throws NumberFormatException, IOException {
		int[] tab = new int[3];
		String tangle = "";
		outStream.write(100);
		outStream.flush();
		while(inStream.available()==0);
		int a = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
		tab[0] = a;
		//add point
		outStream.write(101);
		outStream.flush();
		while(inStream.available()==0);
		int g = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
		tab[1] = g;
		//add point
		outStream.write(102);
		outStream.flush();
		while(inStream.available()==0);
		int d = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
		tab[2] = d;
		outStream.write(99);
		outStream.flush();
		while(inStream.available()==0);
		angle = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
		System.out.println("Angle : " + angle);
		//add point
		outStream.write(12);
		outStream.flush();
		while(inStream.available()==0);
		while(inStream.available()!=0) {
			tangle += Jsoup.parse(inStream.read()+"").text();
		}
		System.out.println("TEST1 : " + tangle);
		
		return tab;
	}
	/**
	 * This method displays the result in a more readable way
	 * @param result This parameter is the integer result given by the neural network
	 */
	public static void afficherResultat(int result) {
		System.out.print("AI Result : ");
		switch(result) {
		case 1:
			System.out.println("Left");
			break;
		case 2:
			System.out.println("Right");
			break;
		case 3:
			System.out.println("Forward");
			break;
		}
	}

	/**
	 * This method is the main thread runner. It loops as long as the Application requires it.
	 * It asks the Arduino for 3 sensors data and then passes it to the network before printing the results and sending an appropriate response to the Arduino.
	 */
	@Override
	public void run() {
		try {
			while(Application.getaiOn()) {
				long start = System.nanoTime();
				int[] response = receive(inStream, outStream);
				long delay = (System.nanoTime() - start);
				trackPosition(delay);
				map.addPoint(new Point((int)posX,(int)posY));
				System.out.println("Right : " + response[2] + " Forward : " + response[0] + " Left : " + response[1] + " Delay : " + delay);
				int result = makeDecision(response[1],response[0],response[2]);
				afficherResultat(result);
				if(result == 2) {
					outStream.write(2);
					isRolling = false;
				}else if(result == 1) {
					outStream.write(4);
					isRolling = false;
				}else {
					outStream.write(1);
					isRolling = true;
				}
				outStream.flush();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public void trackPosition(long delay) {
		if(isRolling) {
			double timeS = delay*Math.pow(10, -9);
			double angleRad = Math.toRadians(angle);
			posX+= (MAXSPEED*timeS)*Math.sin(angleRad)*100;
			posY+= (MAXSPEED*timeS)*Math.cos(angleRad)*100;
			System.out.println("PosX : " + posX + " PosY : "+ posY);
		}
	}
}

package ai;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.jsoup.Jsoup;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.fazecast.jSerialComm.SerialPort;
/**
 * This class is used to load the trained AI and feed it the data gathered from the car sensors. 
 * It's sole purpose is to analyze that data and give an answer according to it.
 * @author Émile Gagné
 */
public class AILoader {

	private final double SCALER = 300.0;
	private MultiLayerNetwork MLN;
	private int testSet1, testSet2, testSet3;
	private INDArray data, newData;
	private SerialPort sp;
	private OutputStream outStream;
	private InputStream inStream;
	
	/**
	 * Launches a series of 3 tests to assert the accuracy of the loaded network and receives data to analyze.
	 * @throws IOException If there is a problem loading the different files for the network (.zip)
	 */
	public void launchAI() throws IOException{
		// Loads the network using the .zip file containing the configuration of the trained network
		MLN = MultiLayerNetwork.load(new File("resources/AI.zip"), false);
		// Create 3 different simulation of inputs to test the results
		testSet1 = makeDecision(30,30,5);
		testSet2 = makeDecision(5,30,30);
		testSet3 = makeDecision(30,30,30);
		
		System.out.print("Results for test 1 [EXPECTED LEFT] : ");
		afficherResultat(testSet1);
		System.out.print("Results for test 2 [EXPECTED RIGHT] : ");
		afficherResultat(testSet2);
		System.out.print("Results for test 3 [EXPECTED FORWARD] : ");
		afficherResultat(testSet3);
		
		sp = SerialPort.getCommPort("com7");
		sp.setComPortParameters(115200, 8, 1, 0);
		sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
		
		if(sp.openPort()) {
			System.out.println("Port open");
		}else {
			System.out.println("Port closed");
			return;
		}

		outStream = sp.getOutputStream();
		inStream = sp.getInputStream();

		System.out.println("Testing with car ...");

		outStream.write(5);
		outStream.flush();

		while(true) {
			int[] response = receive(inStream, outStream);
			System.out.println("Right : " + response[2] + " Forward : " + response[0] + " Left : " + response[1]);
			int result = makeDecision(response[1],response[0],response[2]);
			afficherResultat(result);
			if(result == 2) {
				outStream.write(2);
			}else if(result == 1) {
				outStream.write(4);
			}else {
				outStream.write(1);
			}
			outStream.flush();
		}
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
	public int[] receive(InputStream inStream, OutputStream outStream) throws NumberFormatException, IOException{
		int[] tab = new int[3];
		outStream.write(100);
		outStream.flush();
		while(inStream.available()==0);
		int a = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
		tab[0] = a;
		outStream.write(101);
		outStream.flush();
		while(inStream.available()==0);
		int g = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
		tab[1] = g;
		outStream.write(102);
		outStream.flush();
		while(inStream.available()==0);
		int d = Integer.parseInt(Jsoup.parse(inStream.read()+"").text());
		tab[2] = d;
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

}

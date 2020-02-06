package aapplication;

import java.awt.EventQueue;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Image;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import com.fazecast.jSerialComm.SerialPort;

import ai.AIPilot;
import utillities.DetectionPanel;
import utillities.Mapping2D;
import utillities.TextAreaOutputStream;

import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
/**
 * This is the main application of the ALIVE project, it manages the different threads and allows the user to interact with the car.
 * @author Émile Gagné & Guillaume Blain
 */
public class Application {

	private JFrame frame;
	private JTextField txtConsolein;
	private static OutputStream outStream;
	private static InputStream inStream;
	private static JTextField tfLeft;
	private static JTextField tfMiddle;
	private static JTextField tfRight;
	private JPanel panel_Output = new JPanel();
	private static AIPilot ai;
	private static boolean autoPilotOn = false;
	private static boolean aiOn = false;
	private static MultiLayerNetwork MLN;
	private DetectionPanel detectionPanel = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frame.setVisible(true);
					SerialPort sp = SerialPort.getCommPort("com6");
					sp.setComPortParameters(115200, 8, 1, 0);
					sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

					if(sp.openPort()) {
						System.out.println("Port open");
					}else {
						System.out.println("Port closed");
						return;
					}
					// Loads the network using the .zip file containing the configuration of the trained network
					MLN = MultiLayerNetwork.load(new File("resources/AI.zip"), false);
					outStream = sp.getOutputStream();
					inStream = sp.getInputStream();
					System.out.println("Ready");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1091, 662);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panelControl = new JPanel();
		panelControl.requestFocusInWindow();
		panelControl.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelControl.setBounds(7, 0, 333, 310);
		frame.getContentPane().add(panelControl);
		panelControl.setLayout(null);



		JButton btnUp = new JButton("Up");
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("up");
			}
		});
		btnUp.setBounds(131, 37, 70, 70);
		btnUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				associateImageWithButton(btnUp, "Up2.png",0);
			}public void mouseExited(MouseEvent e) {
				associateImageWithButton(btnUp, "Up.png",0);
			}
			public void mousePressed(MouseEvent e) {
				associateImageWithButton(btnUp, "Up.png",0);
				send(3);
			}public void mouseReleased(MouseEvent e) {
				associateImageWithButton(btnUp, "Up2.png",0);
				send(0);
			}
		});
		panelControl.add(btnUp);

		JButton btnDown = new JButton("Down");
		btnDown.requestFocusInWindow();
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnDown.setBounds(131, 201, 70, 70);
		btnDown.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				associateImageWithButton(btnDown, "Up2.png",2);
			}public void mouseExited(MouseEvent e) {
				associateImageWithButton(btnDown, "Up.png",2);
			}
			public void mousePressed(MouseEvent e) {
				associateImageWithButton(btnDown, "Up.png",2);
				send(1);
			}public void mouseReleased(MouseEvent e) {
				associateImageWithButton(btnDown, "Up2.png",2);
				send(0);
			}
		});
		panelControl.add(btnDown);

		JButton btnRight = new JButton("Right");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnRight.setBounds(219, 120, 70, 70);
		btnRight.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				associateImageWithButton(btnRight, "Up2.png",1);
			}public void mouseExited(MouseEvent e) {
				associateImageWithButton(btnRight, "Up.png",1);
			}
			public void mousePressed(MouseEvent e) {
				associateImageWithButton(btnRight, "Up.png",1);
				send(2);
			}public void mouseReleased(MouseEvent e) {
				associateImageWithButton(btnRight, "Up2.png",1);
				send(0);
			}
		});
		panelControl.add(btnRight);

		JButton btnLeft = new JButton("Left");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnLeft.setBounds(43, 120, 70, 70);
		btnLeft.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				associateImageWithButton(btnLeft, "Up2.png",3);
			}public void mouseExited(MouseEvent e) {
				associateImageWithButton(btnLeft, "Up.png",3);
			}
			public void mousePressed(MouseEvent e) {
				associateImageWithButton(btnLeft, "Up.png",3);
				send(4);
			}public void mouseReleased(MouseEvent e) {
				associateImageWithButton(btnLeft, "Up2.png",3);
				send(0);
			}
		});
		panelControl.add(btnLeft);


		associateImageWithButton(btnUp, "Up.png",0);
		associateImageWithButton(btnRight, "Up.png",1);
		associateImageWithButton(btnDown, "Up.png",2);
		associateImageWithButton(btnLeft, "Up.png",3);

		JCheckBox chckbxModeClavier = new JCheckBox("Keyboard control mode");
		chckbxModeClavier.setSelected(false);
		chckbxModeClavier.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				chckbxModeClavier.setSelected(false);
			}
		});
		chckbxModeClavier.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int c = e.getKeyCode ();
				if (c==KeyEvent.VK_UP) {
					send(1);
				} else if (c==KeyEvent.VK_RIGHT) { 
					send(2);
				} else if (c==KeyEvent.VK_DOWN) { 
					send(3);
				} else if (c==KeyEvent.VK_LEFT) { 
					send(4);
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				send(0);

			}
		});
		chckbxModeClavier.setBounds(6, 280, 321, 23);
		panelControl.add(chckbxModeClavier);


		JPanel panelBTN = new JPanel();
		panelBTN.setBounds(0, 321, 353, 33);
		frame.getContentPane().add(panelBTN);

		JButton btnAutopilotmode = new JButton("Activate Auto-pilot");
		btnAutopilotmode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!autoPilotOn) {
					autoPilotOn = true;
					btnAutopilotmode.setText("Deactive Autopilot");
					send(5);
				}else {
					autoPilotOn = false;
					btnAutopilotmode.setText("Activate Autopilot");
					send(6);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					send(0);
				}
			}
		});
		panelBTN.add(btnAutopilotmode);
		
		JButton btnActivateAi = new JButton("Activate AI");

		btnActivateAi.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				if(!aiOn) {
					aiOn = true;
					try {
						ai = new AIPilot(outStream, inStream, MLN);
						ai.start();
					} catch (IOException e) {}
					btnActivateAi.setText("Deactivate AI");
				}else {
					aiOn = false;
					ai.stop();
					btnActivateAi.setText("Activate AI");
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					send(0);
				}
			}
		});
		panelBTN.add(btnActivateAi);
		
		

		JPanel panelConsole = new JPanel();
		panelConsole.setBounds(0, 365, 532, 247);
		frame.getContentPane().add(panelConsole);
		panelConsole.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 5, 512, 209);
		panelConsole.add(scrollPane);

		JTextArea txtrConsoleout = new JTextArea();
		scrollPane.setViewportView(txtrConsoleout);
		txtrConsoleout.setEditable(false);
		txtrConsoleout.setBackground(Color.WHITE);

		PrintStream out = new PrintStream( new TextAreaOutputStream( txtrConsoleout ) );

		// redirect standard output stream to the TextAreaOutputStream
		System.setOut( out );

		// redirect standard error stream to the TextAreaOutputStream
		System.setErr( out );

		txtConsolein = new JTextField();
		txtConsolein.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					System.out.println(txtConsolein.getText());
					txtConsolein.setText("");
				}
			}
		});
		txtConsolein.setBounds(10, 225, 423, 20);
		panelConsole.add(txtConsolein);
		txtConsolein.setColumns(10);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(txtConsolein.getText());
				txtConsolein.setText("");
			}
		});
		btnSend.setBounds(443, 224, 79, 23);
		panelConsole.add(btnSend);
		panel_Output.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Outputs", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panel_Output.setBounds(732, 0, 333, 310);
		frame.getContentPane().add(panel_Output);
		panel_Output.setLayout(null);

		tfLeft = new JTextField();
		tfLeft.setEditable(false);
		tfLeft.setBounds(66, 54, 86, 20);
		panel_Output.add(tfLeft);
		tfLeft.setColumns(10);

		tfMiddle = new JTextField();
		tfMiddle.setEditable(false);
		tfMiddle.setBounds(66, 85, 86, 20);
		panel_Output.add(tfMiddle);
		tfMiddle.setColumns(10);

		tfRight = new JTextField();
		tfRight.setEditable(false);
		tfRight.setBounds(66, 116, 86, 20);
		panel_Output.add(tfRight);
		tfRight.setColumns(10);

		JLabel lblLeft = new JLabel("Left :");
		lblLeft.setBounds(8, 57, 59, 14);
		panel_Output.add(lblLeft);

		JLabel lblMiddle = new JLabel("Center :");
		lblMiddle.setBounds(8, 88, 59, 14);
		panel_Output.add(lblMiddle);

		JLabel lblRight = new JLabel("Right :");
		lblRight.setBounds(8, 119, 59, 14);
		panel_Output.add(lblRight);

		JButton btnReceive = new JButton("Receive");
		btnReceive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		btnReceive.setBounds(66, 147, 86, 23);
		panel_Output.add(btnReceive);

		JLabel lblSensorsDistance = new JLabel("Distance sensors ");
		lblSensorsDistance.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSensorsDistance.setBounds(8, 28, 144, 14);
		panel_Output.add(lblSensorsDistance);
		
		JPanel detection_panel = new JPanel();
		detection_panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Detection", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		detection_panel.setBounds(346, 0, 380, 310);
		frame.getContentPane().add(detection_panel);
		detection_panel.setLayout(new BorderLayout(0, 0));
		
		try {
			detectionPanel = new DetectionPanel();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		detection_panel.add(detectionPanel);
		
		JSlider slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				detectionPanel.setDistanceG(slider.getValue());
			}
		});
		slider.setBounds(397, 321, 200, 26);
		frame.getContentPane().add(slider);
		detectionPanel.repaint();
		
	}
	public void associateImageWithButton(JButton mainButton, String imageFile, int nbRotation) {
		Image imgRead = null;
		try {
			imgRead = ImageIO.read(new File("resources/"+imageFile));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur pendant la lecture du fichier d'image");
		}

		Image imgRedim = imgRead.getScaledInstance(mainButton.getWidth(), mainButton.getHeight(), Image.SCALE_SMOOTH);
		BufferedImage img = toBufferedImage(imgRedim);
		for(int i = 0;i<nbRotation;i++) {
			img = rotateCw(img);
		}

		mainButton.setOpaque(false);
		mainButton.setContentAreaFilled(false);
		mainButton.setBorderPainted(false);

		mainButton.setText("");
		mainButton.setIcon(new ImageIcon(img));

		imgRead.flush();
		imgRedim.flush();
	}
	public BufferedImage rotateCw(BufferedImage img){
		int width  = img.getWidth();
		int height = img.getHeight();
		BufferedImage newImage = new BufferedImage(height, width, img.getType());

		for(int i=0;i < width; i++) {
			for(int j=0;j < height; j++) {
				newImage.setRGB(height-1-j, i, img.getRGB(i,j));
			}
		}
		return newImage;
	}
	public static BufferedImage toBufferedImage(Image img){
		if (img instanceof BufferedImage){
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}
	public void send(int x){
		try {
			outStream.write(x);
			outStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problem writing");
		}

	}
	public void await(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static OutputStream getOutStream() {
		return outStream;
	}

	public static void setOutStream(OutputStream outStream) {
		Application.outStream = outStream;
	}

	public static InputStream getInStream() {
		return inStream;
	}

	public static void setInStream(InputStream inStream) {
		Application.inStream = inStream;
	}

	public static JTextField getTfLeft() {
		return tfLeft;
	}

	public static void setTfLeft(JTextField tfLeft) {
		Application.tfLeft = tfLeft;
	}

	public static JTextField getTfMiddle() {
		return tfMiddle;
	}

	public static void setTfMiddle(JTextField tfMiddle) {
		Application.tfMiddle = tfMiddle;
	}

	public static JTextField getTfRight() {
		return tfRight;
	}

	public static void setTfRight(JTextField tfRight) {
		Application.tfRight = tfRight;
	}
	public static boolean getautoPilotOn() {
		return autoPilotOn;
	}
	public static boolean getaiOn() {
		return aiOn;
	}
}

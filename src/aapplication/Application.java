package aapplication;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import com.fazecast.jSerialComm.SerialPort;

import ai.AIPilot;
import listeners.DistanceChangedListener;
import threads.GetData;
import utilities.DetectionPanel;
import utilities.TextAreaOutputStream;
/**
 * This is the main application of the ALIVE project, it manages the different threads and allows the user to interact with the car.
 * @author Émile Gagné & Guillaume Blain
 */
public class Application {

	private JFrame frame;
	private static JTextField txtConsolein;
	private static OutputStream outStream;
	private static InputStream inStream;
	private static JTextField tfLeft;
	private static JTextField tfMiddle;
	private static JTextField tfRight;
	private static JPanel panel_Output;
	private static AIPilot ai;
	private static boolean autoPilotOn = false;
	private static boolean aiOn = false;
	private static MultiLayerNetwork MLN;
	private static DetectionPanel panelDetection;
	private static GetData getData;

	
	//Translating variables
	private String language="fr";
	private Locale currentLocale;
	private ResourceBundle texts;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frame.setVisible(true);
					SerialPort sp = SerialPort.getCommPort("com14");
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
					getData = new GetData();
					getData.start();
					getData.addDistanceChangedListener(new DistanceChangedListener() {
						public void distanceChanged(int left, int forward, int right) {
							panelDetection.setDistanceG(left);
							tfLeft.setText(left+"");
							panelDetection.setDistanceA(forward);
							tfMiddle.setText(forward+"");
							panelDetection.setDistanceD(right);	
							tfRight.setText(right+"");
						} 
					});
					System.out.println("Ready");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public Application() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws IOException {
		loadLanguage();
		frame = new JFrame();
		frame.setBounds(100, 100, 1204, 867);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JPanel panelControl = new JPanel();
		panelControl.requestFocusInWindow();
		panelControl.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true),texts.getString("controlPanel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelControl.setBounds(12, 0, 380, 310);
		frame.getContentPane().add(panelControl);
		panelControl.setLayout(null);
		
		JButton btnUp = new JButton("Up");
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("up");
			}
		});
		//test a suppr
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

		JButton btnLeft = new JButton("left");
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
		
		JCheckBox chckbxModeClavier = new JCheckBox(texts.getString("keyboardControl"));
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


		JPanel panelBtn = new JPanel();
		panelBtn.setBounds(0, 321, 339, 33);
		frame.getContentPane().add(panelBtn);

		JButton btnAutopilotmode = new JButton(texts.getString("autoPilotButton"));
		btnAutopilotmode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!autoPilotOn) {
					autoPilotOn = true;
					getData.addDistanceChangedListener(new DistanceChangedListener() {
							
							public void distanceChanged(int left, int forward, int right) {
								panelDetection.setDistanceG(left);
								tfLeft.setText(left+"");
								panelDetection.setDistanceA(forward);
								tfMiddle.setText(forward+"");
								panelDetection.setDistanceD(right);	
								tfRight.setText(right+"");
						}
						});
					btnAutopilotmode.setText(texts.getString("autoPilotButtonOff"));
					send(5);
				}else {
					autoPilotOn = false;
					btnAutopilotmode.setText(texts.getString("autoPilotButton"));
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
		panelBtn.add(btnAutopilotmode);
		
		JButton btnActivateAi = new JButton(texts.getString("aiButton"));

		btnActivateAi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!aiOn) {
					aiOn = true;
					try {
						ai = new AIPilot(outStream, inStream, MLN);
						ai.start();
						ai.addDistanceChangedListener(new DistanceChangedListener() {

							public void distanceChanged(int left, int forward, int right) {
								panelDetection.setDistanceG(left);
								tfLeft.setText(left+"");
								panelDetection.setDistanceA(forward);
								tfMiddle.setText(forward+"");
								panelDetection.setDistanceD(right);	
								tfRight.setText(right+"");
							}
						});

					} catch (IOException e) {}
					btnActivateAi.setText(texts.getString("aiButtonOff"));
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
		panelBtn.add(btnActivateAi);



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
		panel_Output = new JPanel();
		
		panel_Output.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), texts.getString("outputPanel"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panel_Output.setBounds(796, 0, 380, 310);
		frame.getContentPane().add(panel_Output);
		panel_Output.setLayout(null);
		
		JPanel detection_panel = new JPanel();
		detection_panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), texts.getString("detectionPanel"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		detection_panel.setBounds(404, 0, 380, 310);
		frame.getContentPane().add(detection_panel);
		detection_panel.setLayout(null);
								
										tfLeft = new JTextField();
										detection_panel.add(tfLeft);
										tfLeft.setBounds(30, 37, 86, 20);
										tfLeft.setEditable(false);
										tfLeft.setColumns(10);
										
												tfRight = new JTextField();
												tfRight.setBounds(146, 36, 86, 21);
												detection_panel.add(tfRight);
												tfRight.setEditable(false);
												tfRight.setColumns(10);
												
														tfMiddle = new JTextField();
														detection_panel.add(tfMiddle);
														tfMiddle.setBounds(262, 37, 86, 21);
														tfMiddle.setEditable(false);
														tfMiddle.setColumns(10);
														panelDetection = new DetectionPanel();
														panelDetection.setBounds(5, 24, 370, 281);
														detection_panel.add(panelDetection);
								panelDetection.repaint();

		JPanel panelSlider = new JPanel();
		panelSlider.setBounds(349, 321, 377, 33);
		frame.getContentPane().add(panelSlider);
		panelSlider.setLayout(new GridLayout(0, 3, 0, 0));

		JSlider slider = new JSlider();
		panelSlider.add(slider);

		JSlider slider_1 = new JSlider();
		slider_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				panelDetection.setDistanceA((int) (slider_1.getValue() * 1.25));
			}
		});
		panelSlider.add(slider_1);

		JSlider slider_2 = new JSlider();
		slider_2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				panelDetection.setDistanceD((int) (slider_2.getValue() * 1.25));
			}
		});
		panelSlider.add(slider_2);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				panelDetection.setDistanceG((int) (slider.getValue() * 1.25));
			}
		});
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
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
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
	/**
	 * This method loads the good LanguageBundle
	 * @author Olivier
	 */
	private void loadLanguage() {
		currentLocale = new Locale(language);
		texts = ResourceBundle.getBundle("TraductionBundle", currentLocale);
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
	public static boolean getAiOn() {
		return aiOn;
	}
	public static AIPilot getAi(){
		return ai;
	}
}

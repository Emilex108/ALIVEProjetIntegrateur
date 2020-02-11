package aapplication;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import geometry.BoxType;
import geometry.ClassBox;
import videofeed.BoxFeed;
import videofeed.CapteurFeed;
/**
 * This class contains a frame with all the different types of videoFeed we use
 * @author Olivier St-Pierre
 *
 */
public class CameraTab {

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public CameraTab() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1631, 1019);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu menuPrincipal = new JMenu("Menu");
		menuBar.add(menuPrincipal);
		
		JMenuItem mntmInstructions = new JMenuItem("Instructions");
		menuPrincipal.add(mntmInstructions);
		
		JMenuItem mntmPropos = new JMenuItem("\u00C0 propos");
		menuPrincipal.add(mntmPropos);
		
		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		menuPrincipal.add(mntmQuitter);
		frame.getContentPane().setLayout(null);
		
		BoxFeed videoFeed = new BoxFeed();
		videoFeed.setBounds(64, 44, 680, 420);
		frame.getContentPane().add(videoFeed);
		ClassBox voiture = new ClassBox(200, 200, 100, 100, BoxType.PERSON);
		videoFeed.addBox(voiture);
		
		
		CapteurFeed capteurFeed = new CapteurFeed(10, 10, 19);
		capteurFeed.setBounds(870, 44, 680, 420);
		frame.getContentPane().add(capteurFeed);
		
		JButton btnFrame = new JButton("Image par image");
		btnFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				videoFeed.arreter();
				videoFeed.framePreFrame();
			}
		});
		btnFrame.setBounds(173, 525, 225, 23);
		frame.getContentPane().add(btnFrame);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				videoFeed.demarrer();
			}
		});
		btnPlay.setBounds(173, 570, 89, 23);
		frame.getContentPane().add(btnPlay);

		videoFeed.demarrer();
		
		capteurFeed.demarrer();

	}
}

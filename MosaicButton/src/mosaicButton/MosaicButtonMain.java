package mosaicButton;

import java.awt.EventQueue;
import javax.swing.UIManager;

public class MosaicButtonMain {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); // Apply Nimbus Look-and-Feel
					MosaicButtonFrame frame = new MosaicButtonFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}

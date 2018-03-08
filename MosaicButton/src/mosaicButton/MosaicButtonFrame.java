/**
 * Created on Jan 7, 2018
 * @author msjo -- hufs.ac.kr, Dept of CES
 * Copy Right -- Free for Educational Purpose
 */
package mosaicButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.Dimension;
import java.awt.Toolkit;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

import java.awt.Color;
import java.awt.Graphics2D;

public class MosaicButtonFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int IMAGENUM = 25; // This value must be a square number.
	private static final int IMAGENUMSQRT = (int)(Math.sqrt(IMAGENUM));
	private static final int SQUARESIDELENGTH = 150; // This value determines button's size.
	private static final int MOSAICQUALITY = 5; // This value is aliquot part of 'SQUARESIDELENGTH'.

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int FRAME_WIDTH = SQUARESIDELENGTH*IMAGENUMSQRT; // Frame's width
	private static final int FRAME_HEIGHT = SQUARESIDELENGTH*IMAGENUMSQRT; // Frame's height

	MosaicButtonFrame thisClass = this;
	BufferedImage oneImage = null;
	BufferedImage[][] originalImageData = new BufferedImage[IMAGENUMSQRT][IMAGENUMSQRT]; // original image's array which fills each cell
	BufferedImage[][] mosaicImageData = new BufferedImage[MOSAICQUALITY][MOSAICQUALITY]; // mosaic image's array which fills each cell

	private static final String IMAGERESOURCE = "/images/image.png"; // image file's path
	private boolean[][] isMosaic = new boolean [IMAGENUMSQRT][IMAGENUMSQRT]; // cell's status(mosaic or non-mosaic)

	private JPanel contentPane; // including btnPanel
	private JPanel btnPanel; // including btnImage
	private JButton[][] btnImage = new JButton [IMAGENUMSQRT][IMAGENUMSQRT];

	/**
	 * Create the frame.
	 */
	public MosaicButtonFrame() {
		setTitle("Mosaic-Image Button"); // program's title
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT); // set frame's size
		setResizable(false); // fix frame's size
		setLocation((screenSize.width - FRAME_WIDTH) / 2, (screenSize.height - FRAME_HEIGHT) / 2);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		btnPanel = new JPanel();
		contentPane.add(btnPanel, BorderLayout.CENTER);
		btnPanel.setLayout(new GridLayout((int)IMAGENUMSQRT, (int)IMAGENUMSQRT, 0, 0));	

		for(int i=0; i<IMAGENUMSQRT; i++) {
			for(int j=0; j<IMAGENUMSQRT; j++) {
				int m = i; // actionListener needs final-variable
				int n = j; // actionListener needs final-variable
				isMosaic[m][n] = false; // initialize cell's status
				btnImage[i][j] = new JButton();

				JButton btnCell = btnImage[i][j];
				btnCell.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mosaicAction(btnCell, m, n);
					}
				});
				btnPanel.add(btnCell);
			}
		}

		initialize(); // set cell(button)'s icon
	}

	void initialize() {
		try {
			oneImage = ImageIO.read(getClass().getResource(IMAGERESOURCE));
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		int spwidth = oneImage.getWidth()/IMAGENUMSQRT; // sliced picture's width
		int spheight =  oneImage.getHeight()/IMAGENUMSQRT; // sliced picture's height
		int cellWidth = thisClass.getWidth()/IMAGENUMSQRT; // button(cell)'s width
		int cellHeight = thisClass.getHeight()/IMAGENUMSQRT; // button(cell)'s height

		for(int i=0; i<IMAGENUMSQRT; ++i) { // slicing image
			for(int j=0; j<IMAGENUMSQRT; ++j) {
				originalImageData[i][j] = 
						oneImage.getSubimage(j*spwidth, i*spheight, spwidth, spheight);
			}
		}

		for(int i = 0; i < IMAGENUMSQRT; i++) { // set button's image-icon
			for(int j = 0; j < IMAGENUMSQRT; j++) {
				btnImage[i][j].setIcon(new ImageIcon(originalImageData[i][j]
						.getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH))); // resize image
			}
		}
	}

	void mosaicAction(JButton btnCell, int m, int n) {
		int cellWidth = FRAME_WIDTH/IMAGENUMSQRT; // button's width
		int cellHeight = FRAME_HEIGHT/IMAGENUMSQRT; // button's height

		if(isMosaic[m][n]) { // restore original(non-mosaic) image
			btnCell.setIcon(new ImageIcon(originalImageData[m][n]
					.getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH))); // resize image
			isMosaic[m][n] = false; // set cell's status
		}
		else { // mosaic image
			int pwidth = originalImageData[m][n].getWidth()/MOSAICQUALITY;
			int pheight = originalImageData[m][n].getHeight()/MOSAICQUALITY;
			int bwidth = cellWidth/MOSAICQUALITY;
			int bheight = cellHeight/MOSAICQUALITY;

			for(int i=0; i<MOSAICQUALITY; ++i) { // copy original-image into another (BufferedImage)array
				for(int j=0; j<MOSAICQUALITY; ++j) {
					mosaicImageData[i][j] = 
							originalImageData[m][n].getSubimage(j*pwidth, i*pheight, pwidth, pheight);
				}
			}

			Color[][] bcol = new Color[MOSAICQUALITY][MOSAICQUALITY];
			for(int i=0; i<MOSAICQUALITY; ++i) {
				for(int j=0; j<MOSAICQUALITY; ++j) {
					bcol[i][j] = getAverageColor(mosaicImageData[i][j]); // get average-color from BufferedImage
					mosaicImageData[i][j] = OneColorBufferedImage.getBufferedImage(bwidth, bheight, bcol[i][j]);
				}
			}

			btnCell.setIcon(new ImageIcon(getMergedBufferedImage(mosaicImageData)));
			isMosaic[m][n] = true; // set cell's status
		}
	}

	Color getAverageColor(BufferedImage tile){ // get average-color from image
		int twidth = tile.getWidth();
		int theight =  tile.getHeight();
		double pixSize = twidth*theight;
		double sumRed = 0;
		double sumGreen = 0;
		double sumBlue = 0;
		Color pixColor = null;

		for(int i=0; i<theight; ++i) {
			for(int j=0; j<twidth; ++j) {
				pixColor = new Color(tile.getRGB(i,j));
				sumRed += pixColor.getRed();
				sumGreen += pixColor.getGreen();
				sumBlue += pixColor.getBlue();
			}
		}

		int avgRed = (int)(sumRed/pixSize);
		int avgGreen = (int)(sumGreen/pixSize);
		int avgBlue = (int)(sumBlue/pixSize);

		return new Color(avgRed, avgGreen, avgBlue);
	}

	BufferedImage getMergedBufferedImage(BufferedImage[][] imageData) { // merge sliced(2D-Array) image
		int width = SQUARESIDELENGTH;
		int height = SQUARESIDELENGTH;

		BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();

		graphics.setBackground(Color.WHITE);

		for(int i=0; i<MOSAICQUALITY; i++) {
			for(int j=0; j<MOSAICQUALITY; j++) {
				graphics.drawImage(imageData[i][j], imageData[i][j].getWidth()*j, imageData[i][j].getHeight()*i, null);
			}
		}

		return mergedImage;
	}

}

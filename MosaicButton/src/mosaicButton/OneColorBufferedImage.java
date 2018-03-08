package mosaicButton;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class OneColorBufferedImage {
	static public BufferedImage getBufferedImage (int width, int height, Color color){
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] data = new int[width * height];
		int i = 0;
		
		for(int y = 0; y < height; y++) {
			int red = color.getRed();
			int green = color.getGreen();
			int blue = color.getBlue();
			
			for(int x = 0; x < width; x++) {
				data[i++] = (red << 16) | (green << 8) | blue;
			}
		}
		result.setRGB(0, 0, width, height, data, 0, width);

		return result;
	}
}

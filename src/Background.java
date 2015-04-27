import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Background extends GameObj {
	public static final String img_file = "arena.png";
	public static final int SIZE_X = 250;
	public static final int SIZE_Y = 300;


	private static BufferedImage img;

	public Background(int courtWidth, int courtHeight) {
		super(0, 0, 0, 0, courtWidth, courtHeight, 
				courtWidth, courtHeight);
		try {
			if (img == null) {
				img = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(img, pos_x, pos_y, width, height, null);
	}

}
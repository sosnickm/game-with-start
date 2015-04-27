/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A basic game object displayed as a black square, starting in the upper left
 * corner of the game court.
 * 
 */
public class Player extends GameObj {
	public static final int SIZE = 70;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public String img_file;
	private BufferedImage img;
	private int Life;
	private int RorL;

	public Player(int courtWidth, int courtHeight, int INIT_X, int INIT_Y,
			int RorL, int playerChoice) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, SIZE, SIZE, courtWidth,
				courtHeight);
		this.RorL = RorL;
		if (playerChoice == 1 && RorL == 1) {
			img_file = "stand_1.png";
		} else if (playerChoice == 1 && RorL == 2) {
			img_file = "stand_2.png";
		} else if (playerChoice == 2 && RorL == 1) {
			img_file = "block_1.png";
		} else if (playerChoice == 2 && RorL == 2) {
			img_file = "block_2.png";
		} else if (playerChoice == 3 && RorL == 1) {
			img_file = "walk_1.png";
		} else if (playerChoice == 3 && RorL == 2) {
			img_file = "walk_2.png";
		} else if (playerChoice == 4 && RorL == 1) {
			img_file = "uppercut_5.png";
		} else if (playerChoice == 4 && RorL == 2) {
			img_file = "uppercut_2.png";
		}
		try {
			if (img == null) {
				img = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		Life = 10;
	}

	public int getLife() {
		return Life;
	}

	public void gotHit(Projectile projectile) {
		if (!projectile.getHit()) {
			Life = Life - 1;
		}
	}

	public int getShoot() {
		if (this.RorL == 1) {
			return 3;
		} else {
			return -3;
		}
	}

	public int getSide() {
		return this.RorL;
	}

	public int getX() {
		if (this.RorL == 1) {
			return 75;
		} else {
			return 500;
		}
	}

	public int getY() {
		return pos_y;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(img, pos_x, pos_y, width, height, null);
	}

}

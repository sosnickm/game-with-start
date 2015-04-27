import java.awt.Color;
import java.awt.Graphics;

public class Projectile extends GameObj {

	private Player player;
	public static final int SIZE = 20;
	public static final int INIT_VEL_X = 3;
	public static final int INIT_VEL_Y = 0;
	private boolean hit;

	public Projectile(int court_width, int court_height, Player player) {
		super(player.getShoot(), INIT_VEL_Y, player.getX(), player.getY(),
				SIZE, (SIZE / 4), court_width, court_height);

		this.player = player;
		this.hit = false;

	}

	@Override
	public void draw(Graphics g) {
		if (hit == false) {
			g.setColor(Color.YELLOW);
			g.fillRect(pos_x, pos_y, width, height);
		}
	}
	
	public void hitPlayer(GameObj other) {
		if (intersects(other)) {
			hit = true;
		}
	}
	
	public boolean getHit() {
		return hit;
	}
	
	@Override
	public void clip() {
	}
	

	// @Override
	// public int compareTo(Object o) {
	// // TODO Auto-generated method stub
	// return 0;
	// }
}

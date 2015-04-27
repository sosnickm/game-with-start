/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
	private Circle snitch1; // the Golden Snitch, bounces
	private Circle snitch2; // the Golden Snitch, bounces
	private Circle snitch3; // the Golden Snitch, bounces
	private LinkedList<Circle> snitches;
	private Background background;
	private Player playerLeft;
	private Player playerRight;
	private LinkedList<Projectile> projectiles;

	public boolean playing = false; // whether the game is running
	private JLabel status; // Current status text (i.e. Running...)

	// Game constants
	public static final int COURT_WIDTH = 600;
	public static final int COURT_HEIGHT = 500;
	public static final int SQUARE_VELOCITY = 4;
	public static final int PLAYER_VELOCITY = 4;
	// Update interval for timer, in milliseconds
	public static final int INTERVAL = 35;

	public GameCourt(JLabel status) {
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// The timer is an object which triggers an action periodically
		// with the given INTERVAL. One registers an ActionListener with
		// this timer, whose actionPerformed() method will be called
		// each time the timer triggers. We define a helper method
		// called tick() that actually does everything that should
		// be done in a single timestep.
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		// Enable keyboard focus on the court area.
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);

		// This key listener allows the square to move as long
		// as an arrow key is pressed, by changing the square's
		// velocity accordingly. (The tick method below actually
		// moves the square.)

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN)
					playerRight.v_y = PLAYER_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					playerRight.v_y = -PLAYER_VELOCITY;
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN)
					playerRight.v_y = 0;
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					playerRight.v_y = 0;
			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_S)
					playerLeft.v_y = PLAYER_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_W)
					playerLeft.v_y = -PLAYER_VELOCITY;
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_S)
					playerLeft.v_y = 0;
				else if (e.getKeyCode() == KeyEvent.VK_W)
					playerLeft.v_y = 0;
			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_D) {
					Projectile projectile1 = new Projectile(COURT_WIDTH,
							COURT_HEIGHT, playerLeft);
					projectiles.add(projectile1);
				} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					Projectile projectile1 = new Projectile(COURT_WIDTH,
							COURT_HEIGHT, playerRight);
					projectiles.add(projectile1);
				}
			}
		});

		this.status = status;

	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {

		snitch1 = new Circle(COURT_WIDTH, COURT_HEIGHT, 
				(int) (Math.random() * 500), (int) (Math.random() * 600));
		snitch2 = new Circle(COURT_WIDTH, COURT_HEIGHT, 
				(int) (Math.random() * 500), (int) (Math.random() * 600));
		snitch3 = new Circle(COURT_WIDTH, COURT_HEIGHT, 
				(int) (Math.random() * 500), (int) (Math.random() * 600));
		snitches = new LinkedList<Circle>();
		snitches.add(snitch1);
		snitches.add(snitch2);
		snitches.add(snitch3);
		background = new Background(COURT_WIDTH, COURT_HEIGHT);
		playerLeft = new Player(COURT_WIDTH, COURT_HEIGHT, 0, 0, 1, 1);
		playerRight = new Player(COURT_WIDTH, COURT_HEIGHT, 600, 0, 2, 3);
		this.projectiles = new LinkedList<Projectile>();

		playing = true;
		status.setText("Running...");

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		if (playing) {
			// advance the square and snitch in their
			// current direction.
			// square.move();
			playerLeft.move();
			playerRight.move();
			for (Circle element : snitches) {
				element.move();
			}

			for (Projectile element : projectiles) {
				element.move();
				if (element.intersects(playerLeft)) {
					playerLeft.gotHit(element);
				} else if (element.intersects(playerRight)) {
					playerRight.gotHit(element);
				}
				element.hitPlayer(playerLeft);
				element.hitPlayer(playerRight);
				for (Circle snitch : snitches) {
					element.bounce(element.hitObj(snitch));
					//snitch.bounce(snitch.hitObj(element));
				}
			}

			if (playerLeft.getLife() == 0) {
				playing = false;
				status.setText("Player One Loses!");
			} else if (playerRight.getLife() == 0) {
				playing = false;
				status.setText("Player Two Loses!");
			}

			// make the snitch bounce off walls...
			for (Circle snitch : snitches) {
				snitch.bounce(snitch.hitWall());
				// ...and the players
				snitch.bounce(snitch.hitObj(playerLeft));
				snitch.bounce(snitch.hitObj(playerRight));
			}
			// if the projectiles hit a player

			// update the display
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		background.draw(g);
		playerLeft.draw(g);
		playerRight.draw(g);
		for (Circle snitch : snitches) {
			snitch.draw(g);
		}
		// if (!projectiles.isEmpty()) {
		for (Projectile element : projectiles) {
			element.draw(g);
		}
		// }
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}

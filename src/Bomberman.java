import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.ImageIcon;

public class Bomberman extends Thread {
	static final byte down = 0, left = 1, right = 2, up = 3;
	int x = 40, y = 90, width = 40, height = 40;
	volatile byte direction;
	int i, j;
	Map panel;
	boolean isAlive;
	Image bomberDown, bomberLeft, bomberRight, bomberUp;
	int powerupAddition = 0;
	private boolean controlled = false;

	public Bomberman(Map panel) {
		this.isAlive = true;
		this.panel = panel;

		ImageIcon ii;
		ii = new ImageIcon("bomber.png");
		bomberDown = ii.getImage();
		ii = new ImageIcon("bomber_left.png");
		bomberLeft = ii.getImage();
		ii = new ImageIcon("bomber_right.png");
		bomberRight = ii.getImage();
		ii = new ImageIcon("bomber_back.png");
		bomberUp = ii.getImage();

	}

	public void draw(Graphics g, int direction) {// drawing bomberman picture
		switch (direction) {
		case down: {
			g.drawImage(bomberDown, x, y, width, height, null);
			break;
		}
		case left: {
			g.drawImage(bomberLeft, x, y, width, height, null);
			break;
		}
		case right: {
			g.drawImage(bomberRight, x, y, width, height, null);
			break;
		}
		case up: {
			g.drawImage(bomberUp, x, y, width, height, null);
			break;
		}
		}

	}

	void move(int direction) {

		switch (direction) {// increase / decrease values of player location
		case down:
			j++;
			break;
		case left:
			i--;
			break;
		case right:
			i++;
			break;
		case up:
			j--;
			break;
		}

	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(byte direction) {
		this.direction = direction;
	}

	public void run() {
		while (true) {
			// if Paused game
			synchronized (this) {
				if (panel.pauseFlag == 1) {
					try {
						wait();
					} catch (InterruptedException e) {
					}
				}
			}

			if (Map.board[this.i][this.j].isExploded == true) {// if bomberman steps in an explosion
				break;
			}

			if (!colision() && controlled) {// Check for colision with stone / steel walls
				move(direction); // move bomberman a step in current direction
			}
			
			x = Map.blockSize * i;
			y = Map.blockSize * j + 50;
			
			Constants.sleep(250);
			panel.repaint();
		}
		System.out.println("You Lost!");
		System.exit(0);
	}

	public boolean isControlled() {
		return controlled;
	}

	public void setControlled(boolean controlled) {
		this.controlled = controlled;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	private boolean colision() { // check for colision with solid blocks (stone / steel)
		if ((Map.board[this.i + 1][this.j].type == Block.blockTypeSteel
				|| Map.board[this.i + 1][this.j].type == Block.blockTypeStone) && this.direction == right) {
			return true;
		}
		if ((Map.board[this.i][this.j + 1].type == Block.blockTypeSteel
				|| Map.board[this.i][this.j + 1].type == Block.blockTypeStone) && this.direction == down) {
			return true;
		}
		if ((Map.board[this.i - 1][this.j].type == Block.blockTypeSteel
				|| Map.board[this.i - 1][this.j].type == Block.blockTypeStone) && this.direction == left) {
			return true;
		}
		if ((Map.board[this.i][this.j - 1].type == Block.blockTypeSteel
				|| Map.board[this.i][this.j - 1].type == Block.blockTypeStone) && this.direction == up) {
			return true;
		}
		powerup();
		return false;
	}

	void powerup() { // starting powerup if picked up
		if (Map.board[this.i][this.j].type == Block.blockTypePowerup) {
			Map.board[this.i][this.j].type = Block.blockTypeGrass;// delete image & block type to default
			Map.board[this.i][this.j].img = Map.board[this.i][this.j].block_Images[Block.blockTypeGrass];
			panel.p.start();

		}
	}
}

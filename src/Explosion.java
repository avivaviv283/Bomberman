import java.awt.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Explosion extends Thread {
	int x, y, width, height;
	Map panel;
	Image explosionImage;
	int i, j;
	int explosionSize = 3;
	ArrayList<Block> neighbours;

	public Explosion(int x, int y, int width, int height, int i, int j, Map panel) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.panel = panel;
		this.i = i;
		this.j = j;
		ImageIcon ii = new ImageIcon("explosion.png");
		explosionImage = ii.getImage();
		this.neighbours = new ArrayList<Block>();
	}

	public void draw(Graphics g) {
		g.drawImage(explosionImage, x, y, width, height, null);
	}

	public void run() {

		// if Paused game
		synchronized (this) {
			if (panel.pauseFlag == 1) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}

		explode();

		Constants.sleep(1000);
		// if Paused game
		synchronized (this) {
			if (panel.pauseFlag == 1) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}
		clearNeighbours();
		panel.e = null;
	}

	private void clearNeighbours() { // clearing all of explosion of off map
		for (int i = 0; i < neighbours.size(); i++) {
			neighbours.get(i).isExploded = false;
		}

	}

	private void explode() {
		boolean right, left, up, down;
		right = true;
		left = true;
		up = true;
		down = true;
		// Elixploding horizontally
		for (int n = 0; n < explosionSize + panel.players.get(Map.playerIndex).powerupAddition; n++) {
			if (i + n < Map.boardWidth)// not out of bounds
				if (Map.board[this.i + n][this.j].type == Block.blockTypeSteel) {// not colliding with steel block
					right = false;
				} else if (right) {
					Map.board[this.i + n][this.j].isExploded = true;
					this.neighbours.add(Map.board[this.i + n][this.j]);
				}
			if (i - n > 0) {// not out of bounds
				if (Map.board[this.i - n][this.j].type == Block.blockTypeSteel) {// not colliding with steel block
					left = false;
				} else if (left) {
					Map.board[this.i - n][this.j].isExploded = true;
					this.neighbours.add(Map.board[this.i - n][this.j]);
				}
			}
		}
		// Exploding vertically
		for (int n = 0; n < explosionSize + panel.players.get(Map.playerIndex).powerupAddition; n++) {
			if (j + n < Map.boardHeight)// not out of bounds
				if (Map.board[this.i][this.j + n].type == Block.blockTypeSteel) {// not colliding with steel block
					down = false;
				} else if (down) {
					Map.board[this.i][this.j + n].isExploded = true;
					this.neighbours.add(Map.board[this.i][this.j + n]);
				}
			if (j - n > 0) {// not out of bounds
				if (Map.board[this.i][this.j - n].type == Block.blockTypeSteel) {// not colliding with steel block
					up = false;
				} else if (up) {
					Map.board[this.i][this.j - n].isExploded = true;
					this.neighbours.add(Map.board[this.i][this.j - n]);
				}
			}
		}
	}

}

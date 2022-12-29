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

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if Paused game
				synchronized (this) {
					if (panel.pauseFlag == 1) {
						try {
							wait();
						} catch (InterruptedException e) {}
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
		// Exploding horizontaly
		for (int k = 0; k < explosionSize + panel.players.get(panel.playerIndex).powerupAddition; k++) {
			if (i + k < panel.boardHeight) {// not out of bounds
				if (panel.board[this.i + k][this.j].type == Block.blockTypeSteel) {// not colliding with steel block
					break;
				} else {
					panel.board[this.i + k][this.j].isExploded = true;
					this.neighbours.add(panel.board[this.i + k][this.j]);

				}
				if (i - k > 0) { // not out of bounds
					if (panel.board[this.i - k][this.j].type == Block.blockTypeSteel) {// not colliding with steel block
						break;
					} else {
						panel.board[this.i - k][this.j].isExploded = true;
						this.neighbours.add(Map.board[this.i - k][this.j]);

					}
				}
			}
		}
		// Exploding verticaly
		for (int n = 0; n < explosionSize + panel.players.get(panel.playerIndex).powerupAddition; n++) {
			if (j + n < panel.boardWidth)// not out of bounds
				if (panel.board[this.i][this.j + n].type == Block.blockTypeSteel) {// not colliding with steel block
					break;
				} else {
					panel.board[this.i][this.j + n].isExploded = true;
					this.neighbours.add(panel.board[this.i][this.j + n]);
				}
			if (j - n > 0) {// not out of bounds
				if (panel.board[this.i][this.j - n].type == Block.blockTypeSteel) {// not colliding with steel block

					break;
				} else {
					panel.board[this.i][this.j - n].isExploded = true;
					this.neighbours.add(panel.board[this.i][this.j - n]);
				}
			}
		}
	}

}

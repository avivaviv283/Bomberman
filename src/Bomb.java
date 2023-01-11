import java.util.*;

import javax.swing.ImageIcon;

import java.awt.*;

public class Bomb extends Thread {
	int x, y, width, height;
	Map panel;
	Image bombImage;
	Image bombCharging;
	Image img;
	Image explosion;
	int i, j;
	
	
	public Bomb(int x, int y, int width, int height, int pIndex, Map panel) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.panel = panel;
		ImageIcon ii = new ImageIcon("bomb.png");
		this.bombImage = ii.getImage();
		ii = new ImageIcon("bomb_explode.png");
		this.bombCharging = ii.getImage();
		ii = new ImageIcon("explosion.png");
		this.explosion = ii.getImage();
		i = panel.players.get(pIndex).i;
		j = panel.players.get(pIndex).j;
	
	}

	public void draw(Graphics g) {
		g.drawImage(img, x, y, width, height, null);
	}

	public void run() {

		//if Paused game
		synchronized (this) {
			if (panel.pauseFlag == 1) {
				try {
					wait();
				} catch (InterruptedException e) {}
			}
		}
		img = bombImage;
		panel.repaint();
		//if Paused game
				synchronized (this) {
					if (panel.pauseFlag == 1) {
						try {
							wait();
						} catch (InterruptedException e) {}
					}
				}
				Constants.sleep(1000);
		//if Paused game
				synchronized (this) {
					if (panel.pauseFlag == 1) {
						try {
							wait();
						} catch (InterruptedException e) {}
					}
				}
		img = bombCharging;
		panel.repaint();
		Constants.sleep(500);
		//if Paused game
				synchronized (this) {
					if (panel.pauseFlag == 1) {
						try {
							wait();
						} catch (InterruptedException e) {}
					}
				}
		explode();
		panel.b = null;
	}

	private void explode() { // create new explosions
		panel.e = new Explosion(x,y,width,height,this.i,this.j,this.panel);
		panel.e.start();
	}
}

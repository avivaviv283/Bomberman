import java.awt.*;

import javax.swing.ImageIcon;

public class Powerup extends Thread {
	int x, y, width, height;
	Map panel;
	int i, j;
	Image powerupImage, backgroundImage, img;
	int type;
	int powerupTimer = 15000;
	
	public Powerup(int x, int y, int i, int j, int width, int height, Map panel) {
		this.x = x;
		this.y = y;
		this.i = i;
		this.j = j;
		this.width = width;
		this.height = height;
		this.panel = panel;
		ImageIcon ii = new ImageIcon("bomb.png");
		powerupImage = ii.getImage();
		ii = new ImageIcon("grass_background.jpg");
		backgroundImage = ii.getImage();
	}

	public void draw(Graphics g) {
		g.drawImage(img, x, y, width, height, null);
	}

	public void run() {// When powerup is alive

		panel.players.get(panel.playerIndex).powerupAddition += 1;// add explosion size
		try {
			Thread.sleep(powerupTimer);// sleep keeps thread alive, while thread is alive explosion size is increased
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		System.out.println("Powerup ended");
		panel.players.get(panel.playerIndex).powerupAddition -= 1;// Decrease explosion size
		panel.p = null;// delete Powerup
	}

}

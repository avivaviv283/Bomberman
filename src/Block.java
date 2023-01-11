import java.awt.*;
import java.util.*;
import javax.swing.ImageIcon;

public class Block {
	int x, y, width, height;
	Map panel;
	Image[] block_Images;
	int type;
	Image img, saveImage;
	boolean isExploded;

	public static final byte blockTypeSteel = 0, blockTypeGrass = 1, blockTypeStone = 2, blockTypeExplosion = 3,
			blockTypePowerup = 4;
	
	public Block(int x, int y, int width, int height, Map p) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.panel = p;
		this.block_Images = new Image[4];
		this.isExploded = false;
		ImageIcon ii = new ImageIcon("steel_block.png");
		this.block_Images[blockTypeSteel] = ii.getImage();
		ii = new ImageIcon("grass_background.jpg");
		this.block_Images[blockTypeGrass] = ii.getImage();
		ii = new ImageIcon("stone_block.png‬");
		this.block_Images[blockTypeStone] = ii.getImage();

	}

	public void draw(Graphics g) {
		if (this.isExploded == true && panel.e != null) {
			g.drawImage(panel.e.explosionImage, x, y, width, height, null);
		} else {
			g.drawImage(img, x, y, width, height, null);
		}
	}
}

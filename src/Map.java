import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MenuKeyEvent;

import java.util.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Map extends JPanel {
	static Block[][] board;
	ArrayList<Bomberman> players = new ArrayList<Bomberman>();
	static final int boardWidth = 17, boardHeight = 13, blockSize = 40;
	static final int numBombs = 3;

	Bomb[] bombArr;
	Bomb b;
	Explosion e;
	boolean bombPlaced = false;

	Powerup p;

	static int pauseFlag;
	static byte playerIndex;

	Image saveImage;
	Image pauseImage;

	public Map(byte playerIndex, int playerCount) {
		pauseFlag = 0;
		board = new Block[boardWidth][boardHeight];
		this.playerIndex = playerIndex;

		buildBoard();

		this.addKeyListener(new BomberListener());
		setFocusable(true);

		// Create player ArrayList and set the starting location of all players
		initPlayers(playerCount);
		// starting the threads of all players connected
		startPlayers();
	}

	private void initPlayers(int playerCount) {
		// Start players array at 1 instead of 0
		if (players.size() == 0) {
			players.add(0, null);
		}
		// Adding amount of players connected (Recieved from server)
		for (int i = 1; i <= playerCount; i++) {			
			players.add(new Bomberman(this));
			setStartinglocation(i);
		}
		//Set the player which belongs to this client as being controlled by the keyboard 
		//	(and not by data from the server)
		players.get(playerIndex).setControlled(true);
		System.out.println(players);
	}

	private void startPlayers() {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i) != null) {
				players.get(i).start();
			}
		}

	}

	private void setStartinglocation(int index) {// Set starting location of all players
		System.out.println("I am player: " + playerIndex);
		switch (index) {
		case 1:
			players.get(index).i = Constants.player1I;
			players.get(index).j = Constants.player1J;
			break;
		case 2:
			players.get(index).i = Constants.player2I;
			players.get(index).j = Constants.player2J;
			break;
		case 3:
			players.get(index).i = Constants.player3I;
			players.get(index).j = Constants.player3J;
			break;
		}

	}

	private void buildBoard() {
		bombArr = new Bomb[numBombs];
		// Generating block background (steel & grass blocks)
		buildBlockMatrix();

		// Generating X amount of stones randomly on the board
		generateStone(50);

		// Generating the powerup location in the board
		generatePowerup();

	}

	private void buildBlockMatrix() {
		// creating blocks
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardHeight; j++) {
				// Create the blocks that build the board
				board[i][j] = new Block(i * blockSize, j * blockSize + 50, blockSize, blockSize, this);

				if (i == 0 || i == boardWidth - 1 || j == 0 || j == boardHeight - 1) {
					board[i][j].img = board[i][j].block_Images[Block.blockTypeSteel];
					board[i][j].type = Block.blockTypeSteel;
					// Background grass blocks
				} else {
					board[i][j].img = board[i][j].block_Images[Block.blockTypeGrass];
					board[i][j].type = Block.blockTypeGrass;
				}
				// Grid shape steel blocks
				if (i % 2 == 0 && j % 2 == 0) {
					board[i][j].img = board[i][j].block_Images[Block.blockTypeSteel];
					board[i][j].type = Block.blockTypeSteel;

				}
			}
		}

	}

	void generateStone(int numStones) {
		// generating stone location in board
		Image stoneImage;
		ImageIcon ii2 = new ImageIcon("stone_block.png");
		stoneImage = ii2.getImage();
		int rndI, rndJ;
		Random r1 = new Random(12345);
		int counter = 0;
		while (counter <= numStones) {
			rndI = 2 + r1.nextInt(boardWidth - 4);
			rndJ = 2 + r1.nextInt(boardHeight - 4);
			if (board[rndI][rndJ].type == Block.blockTypeGrass) {
				board[rndI][rndJ].img = stoneImage;
				board[rndI][rndJ].type = Block.blockTypeStone;
				counter++;
			}
		}
	}

	void generatePowerup() {
		int rndI, rndJ;
		Random r1;
		// Generating powerup location
		boolean isValid = false;
		r1 = new Random(1234);
		while (isValid == false) {
			rndI = 2 + r1.nextInt(boardHeight - 1);
			rndJ = 2 + r1.nextInt(boardWidth - 1);
			if (board[rndI][rndJ].type == Block.blockTypeGrass) {
				isValid = true;
				this.p = new Powerup(rndI * blockSize, rndJ * blockSize + 50, rndI, rndJ, blockSize, blockSize, this);
				board[rndI][rndJ].img = p.powerupImage;
				board[rndI][rndJ].type = Block.blockTypePowerup;
			}

		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// draw all blocks in board
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardHeight; j++) {

				if (board[i][j].isExploded == true && e != null) {// in case of an explosion
					board[i][j].draw(g);

					if (board[i][j].type == Block.blockTypeStone) {// in case the explosion hits stone block
						board[i][j].type = Block.blockTypeGrass;
						board[i][j].img = board[i][j].block_Images[Block.blockTypeGrass];
					}
					e.draw(g);

				} else {
					// Drawing default board
					board[i][j].draw(g);

				}
			}
		}

		// draw Player
		for (int i = 1; i < players.size(); i++) {
			if (players.get(i) != null) {
				players.get(i).draw(g, players.get(i).getDirection());
			}
		}

		// bomb array drawing for all bombs placed
		int i = 0;
		while (bombArr[i] != null && bombArr[i].isAlive() && i < numBombs - 1) {
			bombArr[i].draw(g);
			i++;
		}
		i = 0;

		// Drawing pause image on screen when game is paused
		if (pauseFlag == 1) {
			ImageIcon ii = new ImageIcon("pause_text.png");
			pauseImage = ii.getImage();
			g.drawImage(pauseImage, getWidth() / 2 - ii.getIconWidth() / 2,
					getHeight() / 2 - ii.getIconHeight() / 2 + 20, null);
			repaint();
		}
	}

	public void summonBomb(int pIndex) {
		// create new bomb when space bar key is pressed and placing it in an array
		b = new Bomb(players.get(pIndex).i * Map.blockSize, players.get(pIndex).j * Map.blockSize + 50, blockSize, blockSize, pIndex, this);
		int i = 0;
		while (bombArr[i] != null && bombArr[i].isAlive()) {
			i++;
		}
		bombArr[i] = b;
		bombArr[i].start();
	}

	public int notifyThreads() {
		// notifies all threads in game after game resumed
		for (int i = 0; i < players.size(); i++)
			if (players.get(i) != null)
				synchronized (players.get(i)) {
					players.get(i).notify();
				}
		for (int i = 0; i < bombArr.length; i++) {
			if (bombArr[i] != null)
				synchronized (bombArr[i]) {
					bombArr[i].notify();
				}
		}
		if (e != null)
			synchronized (e) {
				e.notify();
			}

		return 0;
	}

	// Adding key listeners to arrow keys to move player
	class BomberListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			// Down pressed
			case KeyEvent.VK_DOWN:
				players.get(playerIndex).setDirection(Bomberman.down);
				break;
			// Left pressed
			case KeyEvent.VK_LEFT:
				players.get(playerIndex).setDirection(Bomberman.left);
				break;
			// Right pressed
			case KeyEvent.VK_RIGHT:
				players.get(playerIndex).setDirection(Bomberman.right);
				break;
			// Up pressed
			case KeyEvent.VK_UP:
				players.get(playerIndex).setDirection(Bomberman.up);
				break;
			// Spacebar pressed
			case KeyEvent.VK_SPACE:
				if (!bombPlaced) {
					summonBomb(playerIndex);
					bombPlaced = true;
				}
				break;
			case KeyEvent.VK_P:
				// When player presses P he switches flag that makes all threads pause and
				// resumed depending on flag value
				pauseFlag = ((pauseFlag == 0) ? 1 : notifyThreads());
				ClientEnd.reqPause = true;
				break;
			}
		}

	}

}

// 29-7-2022
// 11-8-2022 update 

// A first framework for an "action game" for 2 players
// communicating data (objects) trough a server
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.*;

public class ClientEnd extends JPanel implements Runnable {

	Socket socket;
	InputStream inputStream;
	OutputStream outputStream;
	ObjectOutputStream objectOutputStream;
	ObjectInputStream objectInputStream;
	Map m;
	Thread t;
	int playerIndex;

	public ClientEnd() throws IOException {
		t = new Thread(this);
		connectToServer(ServerEnd.port);

	}

	public void connectToServer(int port) throws IOException {
		socket = new Socket("localhost", port);

		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();

		objectInputStream = new ObjectInputStream(inputStream);
		objectOutputStream = new ObjectOutputStream(outputStream);

		Data d = null;
		// recieve player index from handler
		try {
			Object o = objectInputStream.readObject();
			if (o instanceof Data) {
				d = (Data) o;
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.playerIndex = d.getPlayerIndex();
//el1
	}

	@Override
	public void run() {
		Data d = new Data(playerIndex);
		while (true) {

			try {
				sendData();
			} catch (IOException e) {
				
				e.printStackTrace();
			}

			getData();
			m.players.get(d.playerIndex).setDirection(d.direction);
		}
	}

	private Data getData() {
		Data d = null;
		Object o;
		try {
			o = objectInputStream.readObject();
			
			if (o instanceof Data) {
				d = (Data) o;
				System.out.println("Object recieved: " + d.direction + " , " + d.playerIndex);
			} else {
				if (o instanceof String) {
					if (o.equals("Add Player")) {
						m.players.add(new Bomberman(m));
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;

	}

	private void sendData() throws IOException {

		Data d = new Data(playerIndex, m.players.get(playerIndex).direction);
		System.out.println("Object sent: " + d.direction + " , " + d.playerIndex);
		objectOutputStream.writeObject(d);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// draw all blocks in board
		for (int i = 0; i < m.boardHeight; i++) {
			for (int j = 0; j < m.boardWidth; j++) {

				if (Map.board[i][j].isExploded == true && m.e != null) {// in case of an explosion
					Map.board[i][j].draw(g);

					if (Map.board[i][j].type == Block.blockTypeStone) {// in case the explosion hits stone block
						Map.board[i][j].type = Block.blockTypeGrass;
						Map.board[i][j].img = Map.board[i][j].block_Images[Block.blockTypeGrass];
					}
					m.e.draw(g);

				} else {
					m.board[i][j].draw(g);
				}
			}
		}

		// draw Player//eli
		for (int i = 1; i < m.players.size(); i++) {
			if (m.players.get(i) != null) {
				m.players.get(i).draw(g, m.players.get(i).getDirection());
			}
		}

		// bomb array drawing
		int i = 0;
		while (m.bombArr[i] != null && m.bombArr[i].isAlive() && i < Map.numBombs - 1) {
			m.bombArr[i].draw(g);
			i++;
		}
		i = 0;

		// draw pause image when game pause activated
		//VAMOSOOOOSOSO
		if (Map.pauseFlag == 1) {
			ImageIcon ii = new ImageIcon("pause_text.png");
			m.pauseImage = ii.getImage();
			g.drawImage(m.pauseImage, getWidth() / 2 - ii.getIconWidth() / 2,
					getHeight() / 2 - ii.getIconHeight() / 2 + 20, null);
			repaint();
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ClientEnd e = new ClientEnd();
		JFrame f = new JFrame("BomberMan");
		Object obj;
		obj = e.objectInputStream.readObject();
		if (obj instanceof Integer) {
			// SUSSY WUSSY ^_^ OwO
			int playerCount = (Integer) obj;
			System.out.println("recireved player count: " + playerCount);
			e.m = new Map(e.playerIndex, playerCount);
			f.add(e.m);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setSize(700, 620);
			f.setResizable(false);
			f.setVisible(true);
			f.setFocusable(false);
			//SUSSY WUSSY UwU
			e.t.start();
		}
	}

}

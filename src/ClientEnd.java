
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
	byte playerIndex;

	static boolean reqPause = false;

	public ClientEnd() throws IOException {
		t = new Thread(this);
		connectToServer(ServerEnd.port);
	}

	public void connectToServer(int port) throws IOException {
		socket = new Socket("localhost", port);

		inputStream = socket.getInputStream();
		objectInputStream = new ObjectInputStream(inputStream);

		outputStream = socket.getOutputStream();
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
	}

	@Override
	
	public void run() {
		while (socket.isConnected()) {
			try {
				Constants.sleep(1);
				sendData(); // sel1nd data to everyone else except the one who sent
			} catch (IOException e) {

				e.printStackTrace();
			}
			Constants.sleep(1);
			Data dRecieved = null;
			dRecieved = getData(); // Reading data from input stream
			if (dRecieved != null && dRecieved.direction != null) {
				switch (dRecieved.direction) {
				case Constants.CODE_BOMB:
					sendBombs(dRecieved.playerIndex);
					break;
				case Constants.CODE_PAUSE:
					Map.pauseFlag = 1;
					break;
				case Constants.CODE_NOTIFY:
					Map.pauseFlag = 0;
					m.notifyThreads();
					break;
				default:
					m.players.get(dRecieved.playerIndex).setDirection(dRecieved.direction);
					m.players.get(dRecieved.playerIndex).setI(dRecieved.getI());
					m.players.get(dRecieved.playerIndex).setJ(dRecieved.getJ());
				}
			}
		}
	}

	// Sends to all clients to summon a bomb in the current player location of
	// playerIndex
	// Sends everyone but himself.
	private void sendBombs(int pIndex) {
		for (int i = 1; i < m.players.size(); i++) {
			if (i != pIndex) {
				m.players.get(i).panel.summonBomb(pIndex);
			}
		}
	}

	private Data getData() {
		Data d = null;
		Object o;
		try {
			// reliads info from handlers (might be Data or String)
			o = objectInputStream.readObject();

			if (o instanceof Data) {
				d = (Data) o;
			} else {
				if (o instanceof String) {
					if (o.equals("Add Player")) {
						m.players.add(new Bomberman(m));
					}
				}
			}
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return d;

	}

	private void sendData() throws IOException {
		Data d = null;
		if (m.bombPlaced) {
			// -1 direction tells the other client to summon a bomb on the player with index
			// playerIndex
			d = new Data(playerIndex, Constants.CODE_BOMB);
			objectOutputStream.writeObject(d);
			m.bombPlaced = false;
		} else if (reqPause) {
			if (Map.pauseFlag == 1) {
				d = new Data(playerIndex, Constants.CODE_PAUSE);
				objectOutputStream.writeObject(d);
			} else {
				d = new Data(playerIndex, Constants.CODE_NOTIFY);
				objectOutputStream.writeObject(d);
			}
			reqPause = false;
		} else {
			// Send current direction to all other clients
			d = new Data(playerIndex, m.players.get(playerIndex).direction, (byte) m.players.get(playerIndex).i,
					(byte) m.players.get(playerIndex).j);
			if (d != null && d.direction != null) {
				objectOutputStream.writeObject(d);
			}
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ClientEnd e = new ClientEnd();
		JFrame f = new JFrame("BomberMan");
		Object obj;
		// Reads amount of players connected from server
		obj = e.objectInputStream.readObject();
		if (obj instanceof Integer) {
			int playerCount = (Integer) obj;
			e.m = new Map(e.playerIndex, playerCount);
			f.add(e.m);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setSize(700, 620);
			f.setResizable(false);
			f.setVisible(true);
			f.setFocusable(false);
			e.t.start();
		}
	}

}

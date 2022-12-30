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
		Data d = new Data(playerIndex);
		while (true) {

			try {
				sleep(100);
				sendData();
			} catch (IOException e) {

				e.printStackTrace();
			}

			sleep(100);
			Data dRecieved = null;
			dRecieved = getData();
			if (dRecieved != null && dRecieved.direction != null) {
				m.players.get(dRecieved.playerIndex).setDirection(dRecieved.direction);
			}
		}
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Data getData() {
		Data d = null;
		Object o;
		try {
			System.out.println("Trying to read object");
			o = objectInputStream.readObject();
			System.out.println("succesfully read object");
			if (o instanceof Data) {

				d = (Data) o;
				System.out.println("Object recieved from player: " + d.playerIndex + ": " + d.direction);
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
		Data newd = new Data(d);
		System.out.println("Object sent: " + d.direction + " , " + d.playerIndex);
		objectOutputStream.writeObject(newd);
		System.out.println("Object sent successfully");
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ClientEnd e = new ClientEnd();
		JFrame f = new JFrame("BomberMan");
		Object obj;
		obj = e.objectInputStream.readObject();
		if (obj instanceof Integer) {
			int playerCount = (Integer) obj;
			System.out.println("recireved player count: " + playerCount);
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

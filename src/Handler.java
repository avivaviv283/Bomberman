import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Handler extends Thread {

	static ArrayList<Handler> clients = new ArrayList<>();
	static ArrayList<Data> data = new ArrayList<>();
	private Socket socket;
	InputStream inputStream;
	ObjectInputStream objectinputStream;
	OutputStream outputStream;
	ObjectOutputStream objectOutputStream;

	public Handler(Socket socket) {
		try {
			this.socket = socket;

			this.outputStream = this.socket.getOutputStream();
			this.objectOutputStream = new ObjectOutputStream(this.outputStream);

			this.inputStream = this.socket.getInputStream();
			this.objectinputStream = new ObjectInputStream(inputStream);

			addClientToArray();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void addClientToArray() throws IOException {
		// start array at index 1 instead of 0
		if (clients.size() == 0) {
			clients.add(0, null);
		}
		// adds current handler to handler Array
		clients.add(this);

		// build new data for new client (player index)
		Data d = new Data(clients.indexOf(this));

		// start array at index 1 instead of 0
		if (data.size() == 0) {
			data.add(0, null);
		}

		data.add(d);

		// send player index to client
		clients.get(data.indexOf(d)).objectOutputStream.writeObject(d);

	}

	public void run() {

		while (true) {

			for (int i = 1; i < clients.size(); i++) {
				// Recieves data from one client at a time
				sleep(100);
				Data d = getData(i);
				// send client data to all clients connected
				sleep(100);
				sendData(d);
			}
		}
	}

	public Data getData(int index) {
		// recieves data from a single client
		Data d = new Data(index);

		try {
			System.out.println("Trying to read object");
			Object o = clients.get(index).objectinputStream.readObject();
			System.out.println("Read object!");
			if (o instanceof Data) {
				d = (Data) o;
				System.out.println("Object recieved from player: " + d.playerIndex + "is: " + d.direction);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalError e) {
			e.printStackTrace();
		}

		return d;
	}

	static public void sendData(Integer num) {
		// iterate over all client
		for (int i = 1; i < clients.size(); i++) {
			try {
				// send numerical data to each client (playerCounter, usually)
				clients.get(i).objectOutputStream.writeObject(num);
			} catch (IOException e) {
				// TODO WHATEVER
				e.printStackTrace();
			}
		}
	}

	static public void sendData(Data d) {
		// sends data to all clients except himself
		for (int i = 1; i < clients.size(); i++) {
			try {
				if (d.playerIndex != i) {
					Data newd = new Data(d);
					System.out.println("Sends data to all clients from: " + d.playerIndex + " , " + d.direction);
					clients.get(i).objectOutputStream.writeObject(newd);
					System.out.println("Successfully sent!");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	static public void sendData(String s) {
		// sends data to all clients
		for (int i = 0; i < clients.size(); i++) {
			try {
				clients.get(i).objectOutputStream.writeObject(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
}

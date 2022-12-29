import java.io.Serializable;

public class Data implements Serializable {
	//eli
	private static final long serialVersionUID = 1L;
	volatile int direction;
	int playerIndex;

	public Data(int playerIndex) {
		this.playerIndex = playerIndex;

	}

	public Data(int playerIndex, int direction) {
		this.playerIndex = playerIndex;
		this.direction = direction;

	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

}

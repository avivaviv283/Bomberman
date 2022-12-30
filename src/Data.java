import java.io.Serializable;

public class Data implements Serializable {

	private static final long serialVersionUID = 1L;
	volatile Integer direction;
	Integer playerIndex;

	public Data(int playerIndex) {
		this.playerIndex = playerIndex;

	}

	public Data(int playerIndex, int direction) {
		this.playerIndex = playerIndex;
		this.direction = direction;

	}

	public Data(Data d) {
		this.direction = d.direction;
		this.playerIndex = d.playerIndex;
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

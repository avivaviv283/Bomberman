import java.io.Serializable;

public class Data implements Serializable {

	private static final long serialVersionUID = 1L;
	volatile Integer direction;
	Integer playerIndex;

	public Data(Integer playerIndex) {
		this.playerIndex = playerIndex;

	}

	public Data(Integer playerIndex, Integer direction) {
		this.playerIndex = playerIndex;
		this.direction = direction;

	}

	public Data(Data d) {
		this.direction = d.direction;
		this.playerIndex = d.playerIndex;
	}

	public Integer getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(Integer playerIndex) {
		this.playerIndex = playerIndex;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

}

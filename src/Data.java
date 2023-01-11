import java.io.Serializable;

public class Data implements Serializable {

	private static final long serialVersionUID = 1L;
	volatile Byte direction;
	Byte playerIndex;
	Byte I = 0;
	Byte J = 0;

	public Data(Byte playerIndex) {
		this.playerIndex = playerIndex;

	}

	public Data(Byte playerIndex, Byte direction) {
		this.playerIndex = playerIndex;
		this.direction = direction;

	}
		
	public Data(Data d) {
		this.direction = d.direction;
		this.playerIndex = d.playerIndex;
	}
	
	public Data(Byte playerIndex, Byte direction, Byte I, Byte J) {
		this.playerIndex = playerIndex;
		this.direction = direction;
		this.I = I;
		this.J = J;
	}

	public Byte getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(Byte playerIndex) {
		this.playerIndex = playerIndex;
	}

	public Byte getDirection() {
		return direction;
	}

	public void setDirection(Byte direction) {
		this.direction = direction;
	}

	public Byte getI() {
		return I;
	}

	public void setI(Byte i) {
		I = i;
	}

	public Byte getJ() {
		return J;
	}

	public void setJ(Byte j) {
		J = j;
	}

}

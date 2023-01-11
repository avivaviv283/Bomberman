
public final class Constants {

	public static final int player1I = 1;
	public static final int player1J = 1;
	public static final int player2I = Map.boardWidth - 2;
	public static final int player2J = Map.boardHeight - 2;
	public static final int player3I = 1;
	public static final int player3J = Map.boardHeight - 2;
	public static final byte CODE_BOMB = -1;
	public static final byte CODE_PAUSE = -2;
	public static final byte CODE_NOTIFY = -3;
//	public final int player4I = 1;
//	public final int player4J = 1;

	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

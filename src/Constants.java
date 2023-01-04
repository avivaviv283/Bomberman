
public final class Constants {

	public static final int player1I = 1;
	public static final int player1J = 1;
	public static final int player2I = Map.boardHeight - 2;
	public static final int player2J = Map.boardWidth - 2;
	public static final int player3I = 1;
	public static final int player3J = Map.boardWidth - 2;
	public static final int CODE_BOMB = -1;
	public static final int CODE_PAUSE = -2;
	public static final int CODE_NOTIFY = -3;
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
	
	public static void sleep(int time, int nano) {
		try {
			Thread.sleep(time, nano);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

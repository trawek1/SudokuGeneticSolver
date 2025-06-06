import org.tinylog.Logger;

public class SolvingInfo {
	private static final long TIME_COUNTING_STOPPED = 0;
	private static final long TIME_UPDATE_INTERVAL = 1000;
	private static boolean solvingInfoChanged;
	private static long solvingTimeStart;
	private static long solvingTimeStop;
	private static long lastUpdateTime;
	private static SolvingStatusEnum solvingInfoStatus;
	private static String solvingInfoTimeTxt;
	private static String solvingInfoDetails1Txt;
	private static String solvingInfoDetails2Txt;
	private static String solvingInfoDetails3Txt;

	static {
		changeStatusTo(SolvingStatusEnum.NOT_STARTED);
	}

	public static void resetData() {
		solvingInfoChanged = true;
		solvingTimeStart = TIME_COUNTING_STOPPED;
		solvingTimeStop = TIME_COUNTING_STOPPED;
		lastUpdateTime = TIME_COUNTING_STOPPED;
		solvingInfoTimeTxt = "";
		solvingInfoDetails1Txt = "";
		solvingInfoDetails2Txt = "";
		solvingInfoDetails3Txt = "";
	}

	public static boolean isChanged() {
		return solvingInfoChanged;
	}

	public static void setToNoChanged() {
		solvingInfoChanged = false;
	}

	public static boolean isUpdateTime() {
		if (lastUpdateTime == TIME_COUNTING_STOPPED) {
			return true;
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastUpdateTime > TIME_UPDATE_INTERVAL) {
			lastUpdateTime = currentTime;
			return true;
		}

		return false;
	}

	public static SolvingStatusEnum getStatus() {
		return solvingInfoStatus;
	}

	public static void changeStatusTo(SolvingStatusEnum _status) {
		solvingInfoStatus = _status;
		solvingInfoChanged = true;
		// TODO >>>>> uzupełnić akcje wg statusu
		switch (_status) {
			case NOT_STARTED:
				Logger.info("Ustawiono status: NOT_STARTED.");
				resetData();
				break;
			case IN_PROGRESS:
				Logger.info("Ustawiono status: IN_PROGRESS.");
				resetData();
				lastUpdateTime = System.currentTimeMillis();
				solvingTimeStart = System.currentTimeMillis();
				break;
			case COMPLETED:
				Logger.info("Ustawiono status: COMPLETED.");
				lastUpdateTime = TIME_COUNTING_STOPPED;
				solvingTimeStop = System.currentTimeMillis();
				// TODO >>>> pozbierać zmiany
				solvingInfoChanged = true;
				break;
			case FAILED:
				Logger.info("Ustawiono status: FAILED.");
				lastUpdateTime = TIME_COUNTING_STOPPED;
				solvingTimeStop = System.currentTimeMillis();
				// TODO >>>> pozbierać zmiany
				solvingInfoChanged = true;
				break;
			case STOPPED_BY_USER:
				Logger.info("Ustawiono status: STOPPED_BY_USER.");
				lastUpdateTime = TIME_COUNTING_STOPPED;
				solvingTimeStop = System.currentTimeMillis();
				// TODO >>>> pozbierać zmiany
				solvingInfoChanged = true;
				break;
			case STOPPED_BY_TIMEOUT:
				Logger.info("Ustawiono status: STOPPED_BY_TIMEOUT.");
				lastUpdateTime = TIME_COUNTING_STOPPED;
				solvingTimeStop = System.currentTimeMillis();
				// TODO >>>> pozbierać zmiany
				solvingInfoChanged = true;
				break;
			case STOPPED_BY_GENERATION_LIMIT:
				Logger.info("Ustawiono status: STOPPED_BY_GENERATION_LIMIT.");
				lastUpdateTime = TIME_COUNTING_STOPPED;
				solvingTimeStop = System.currentTimeMillis();
				// TODO >>>> pozbierać zmiany
				solvingInfoChanged = true;
				break;
		}
	}

	public static String getSolvingInfoStatus() {
		return solvingInfoStatus.getDisplayName();
	}

	public static String getSolvingInfoTime() {
		return solvingInfoTimeTxt;
	}

	public static String getSolvingInfoDetails1() {
		return solvingInfoDetails1Txt;
	}

	public static String getSolvingInfoDetails2() {
		return solvingInfoDetails2Txt;
	}

	public static String getSolvingInfoDetails3() {
		return solvingInfoDetails3Txt;
	}

}

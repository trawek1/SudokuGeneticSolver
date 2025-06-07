import org.tinylog.Logger;

public class SolverInfo {
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

	public static boolean isChangedAndReset() {
		if (solvingInfoChanged) {
			solvingInfoChanged = false;
			return true;
		}
		return false;
	}

	public static boolean isUpdateTime() {
		if (solvingInfoStatus != SolvingStatusEnum.IN_PROGRESS) {
			return false;
		}
		if (lastUpdateTime == TIME_COUNTING_STOPPED) {
			return false;
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastUpdateTime < TIME_UPDATE_INTERVAL) {
			return false;
		}

		calculateSolvingTime();
		lastUpdateTime = currentTime;
		return true;
	}

	public static void calculateSolvingTime() {
		if (solvingTimeStart == TIME_COUNTING_STOPPED) {
			return;
		}

		long currentTime;
		if (solvingTimeStop == TIME_COUNTING_STOPPED) {
			currentTime = System.currentTimeMillis() - solvingTimeStart;
		} else {
			currentTime = solvingTimeStop - solvingTimeStart;
		}

		currentTime /= 1_000;
		long minutes = (long) currentTime / 60;
		long seconds = (long) currentTime % 60;

		long maxTime = SolverBase.MAX_SOLVING_TIME / 1_000;
		long minutesMax = (long) maxTime / 60;
		long secondsMax = (long) maxTime % 60;
		solvingInfoTimeTxt = String.format("%02dm %02ds / %02dm %02ds",
				minutes, seconds, minutesMax, secondsMax);

		solvingInfoChanged = true;
		lastUpdateTime = currentTime;
	}

	public static boolean isSolvingTimeExceeded() {
		if (solvingInfoStatus != SolvingStatusEnum.IN_PROGRESS) {
			return false;
		}
		if (solvingTimeStart == TIME_COUNTING_STOPPED) {
			return false;
		}
		long currentTime = System.currentTimeMillis();
		if (currentTime - solvingTimeStart < SolverBase.MAX_SOLVING_TIME) {
			return false;
		}
		solvingTimeStop = currentTime;
		solvingInfoChanged = true;
		return true;
	}

	public static SolvingStatusEnum getStatus() {
		return solvingInfoStatus;
	}

	public static void changeStatusTo(SolvingStatusEnum _status) {
		solvingInfoStatus = _status;
		solvingInfoChanged = true;
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
				// TODO >>>>> uzupełnić akcje wg statusu
				break;
			case COMPLETED:
				Logger.info("Ustawiono status: COMPLETED.");
				lastUpdateTime = TIME_COUNTING_STOPPED;
				solvingTimeStop = System.currentTimeMillis();
				// TODO >>>>> uzupełnić akcje wg statusu
				solvingInfoChanged = true;
				break;
			case FAILED:
				Logger.info("Ustawiono status: FAILED.");
				lastUpdateTime = TIME_COUNTING_STOPPED;
				solvingTimeStop = System.currentTimeMillis();
				// TODO >>>>> uzupełnić akcje wg statusu
				solvingInfoChanged = true;
				break;
			case STOPPED_BY_USER:
				Logger.info("Ustawiono status: STOPPED_BY_USER.");
				lastUpdateTime = TIME_COUNTING_STOPPED;
				solvingTimeStop = System.currentTimeMillis();
				// TODO >>>>> uzupełnić akcje wg statusu
				solvingInfoChanged = true;
				break;
			case STOPPED_BY_TIMEOUT:
				Logger.info("Ustawiono status: STOPPED_BY_TIMEOUT.");
				lastUpdateTime = TIME_COUNTING_STOPPED;
				solvingTimeStop = System.currentTimeMillis();
				calculateSolvingTime();
				// TODO >>>>> uzupełnić akcje wg statusu
				solvingInfoChanged = true;
				break;
			case STOPPED_BY_GENERATION_LIMIT:
				Logger.info("Ustawiono status: STOPPED_BY_GENERATION_LIMIT.");
				lastUpdateTime = TIME_COUNTING_STOPPED;
				solvingTimeStop = System.currentTimeMillis();
				// TODO >>>>> uzupełnić akcje wg statusu
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

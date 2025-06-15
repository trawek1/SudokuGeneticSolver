import org.tinylog.Logger;

public class SolverInfo {
	private static final long TIME_COUNTING_STOPPED = 0;
	private static final long TIME_UPDATE_INTERVAL = 1000;
	private static boolean infoChanged;
	private static long timeSolvingStart;
	private static long timeSolvingStop;
	private static long timeLastUpdate;
	private static SolvingStatusEnum solvingInfoStatus;
	private static String txtTime;
	private static String txtDetails1;
	private static String txtDetails2;
	private static String txtDetails3;

	static {
		setStatusTo(SolvingStatusEnum.NOT_STARTED);
	}

	public static void resetData() {
		infoChanged = true;
		timeSolvingStart = TIME_COUNTING_STOPPED;
		timeSolvingStop = TIME_COUNTING_STOPPED;
		timeLastUpdate = TIME_COUNTING_STOPPED;
		txtTime = "";
		txtDetails1 = "";
		txtDetails2 = "";
		txtDetails3 = "";
	}

	public static boolean isChangedAndReset() {
		if (infoChanged) {
			infoChanged = false;
			return true;
		}
		return false;
	}

	public static boolean isUpdateTime() {
		if (solvingInfoStatus != SolvingStatusEnum.IN_PROGRESS) {
			return false;
		}
		if (timeLastUpdate == TIME_COUNTING_STOPPED) {
			return false;
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime - timeLastUpdate < TIME_UPDATE_INTERVAL) {
			return false;
		}

		calculateSolvingTime();
		return true;
	}

	public static void calculateSolvingTime() {
		if (timeSolvingStart == TIME_COUNTING_STOPPED) {
			return;
		}

		long currentTime;
		if (timeSolvingStop == TIME_COUNTING_STOPPED) {
			currentTime = System.currentTimeMillis() - timeSolvingStart;
		} else {
			currentTime = timeSolvingStop - timeSolvingStart;
		}

		currentTime /= 1_000;
		long minutes = (long) currentTime / 60;
		long seconds = (long) currentTime % 60;

		long maxTime = SolverBase.SOLVING_TIME_MAX / 1_000;
		long minutesMax = (long) maxTime / 60;
		long secondsMax = (long) maxTime % 60;
		txtTime = String.format("%02dm %02ds / %02dm %02ds",
				minutes, seconds, minutesMax, secondsMax);

		infoChanged = true;
		timeLastUpdate = System.currentTimeMillis();
	}

	public static boolean isSolvingTimeExceeded() {
		if (solvingInfoStatus != SolvingStatusEnum.IN_PROGRESS) {
			return false;
		}
		if (timeSolvingStart == TIME_COUNTING_STOPPED) {
			return false;
		}
		long currentTime = System.currentTimeMillis();
		if (currentTime - timeSolvingStart < SolverBase.SOLVING_TIME_MAX) {
			return false;
		}
		timeSolvingStop = currentTime;
		infoChanged = true;
		return true;
	}

	public static SolvingStatusEnum getStatus() {
		return solvingInfoStatus;
	}

	public static void setStatusTo(SolvingStatusEnum _status) {
		solvingInfoStatus = _status;
		infoChanged = true;
		switch (_status) {
			case NOT_STARTED:
				Logger.info("Ustawiono status: NOT_STARTED.");
				resetData();
				break;
			case STARTED:
				Logger.info("Ustawiono status: STARTED.");
				resetData();
				timeLastUpdate = System.currentTimeMillis();
				timeSolvingStart = System.currentTimeMillis();
				calculateSolvingTime();
				SolverInfo.setStatusTo(SolvingStatusEnum.IN_PROGRESS);
				break;
			case IN_PROGRESS:
				Logger.info("Ustawiono status: IN_PROGRESS.");
				timeLastUpdate = System.currentTimeMillis();
				calculateSolvingTime();
				break;
			case COMPLETED:
				Logger.info("Ustawiono status: COMPLETED.");
				timeLastUpdate = TIME_COUNTING_STOPPED;
				timeSolvingStop = System.currentTimeMillis();
				calculateSolvingTime();
				SolverBase.saveSolvingDataToFile("=== COMPLETED");
				infoChanged = true;
				break;
			case ITERATION_COMPLETED:
				Logger.info("Ustawiono status: ITERATION COMPLETED.");
				timeLastUpdate = System.currentTimeMillis();
				SolverBase.saveSolvingDataToFile("=== ITERATION COMPLETED");
				setStatusTo(SolvingStatusEnum.IN_PROGRESS);
				infoChanged = true;
				break;
			case FAILED:
				Logger.info("Ustawiono status: FAILED.");
				timeLastUpdate = TIME_COUNTING_STOPPED;
				timeSolvingStop = System.currentTimeMillis();
				calculateSolvingTime();
				SolverBase.saveSolvingDataToFile("=== FAILED");
				infoChanged = true;
				break;
			case STOPPED_BY_USER:
				Logger.info("Ustawiono status: STOPPED_BY_USER.");
				timeLastUpdate = TIME_COUNTING_STOPPED;
				timeSolvingStop = System.currentTimeMillis();
				calculateSolvingTime();
				SolverBase.saveSolvingDataToFile("=== STOPPED BY USER");
				infoChanged = true;
				break;
			case STOPPED_BY_TIMEOUT:
				Logger.info("Ustawiono status: STOPPED_BY_TIMEOUT.");
				timeLastUpdate = TIME_COUNTING_STOPPED;
				timeSolvingStop = System.currentTimeMillis();
				calculateSolvingTime();
				SolverBase.saveSolvingDataToFile("=== STOPPED BY TIMEOUT");
				infoChanged = true;
				break;
			case STOPPED_BY_GENERATIONS_LIMIT:
				Logger.info("Ustawiono status: STOPPED_BY_GENERATIONS_LIMIT.");
				timeLastUpdate = TIME_COUNTING_STOPPED;
				timeSolvingStop = System.currentTimeMillis();
				calculateSolvingTime();
				SolverBase.saveSolvingDataToFile("=== STOPPED BY GENERATIONS LIMIT");
				infoChanged = true;
				break;
			case ERROR:
				Logger.info("Ustawiono status: ERROR.");
				SolverBase.saveSolvingDataToFile("=== ERROR");
				infoChanged = true;
				break;
		}
	}

	public static String getTxtStatus() {
		return solvingInfoStatus.getDisplayName();
	}

	public static String getTxtTime() {
		return txtTime;
	}

	public static String getTxtDetails1() {
		return txtDetails1;
	}

	public static String getTxtDetails2() {
		return txtDetails2;
	}

	public static String getTxtDetails3() {
		return txtDetails3;
	}

	public static void addDetails(String _details) {
		txtDetails3 = txtDetails2;
		txtDetails2 = txtDetails1;
		txtDetails1 = _details;
	}

	public static void addDetailsAndShow(String _details) {
		txtDetails3 = txtDetails2;
		txtDetails2 = txtDetails1;
		txtDetails1 = _details;
		infoChanged = true;
	}
}

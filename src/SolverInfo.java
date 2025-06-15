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
		SolvingStatusEnum oldStatus = solvingInfoStatus;
		solvingInfoStatus = _status;
		infoChanged = true;
		switch (solvingInfoStatus) {
			case NOT_STARTED:
				resetData();
				break;
			case STARTED:
				SolverBase.saveSolvingDataToFile("=== status: " + oldStatus + " >>> " + solvingInfoStatus);
				resetData();
				timeLastUpdate = System.currentTimeMillis();
				timeSolvingStart = System.currentTimeMillis();
				calculateSolvingTime();
				setStatusTo(SolvingStatusEnum.IN_PROGRESS);
				break;
			case IN_PROGRESS:
				SolverBase.saveSolvingDataToFile("=== status: " + oldStatus + " >>> " + solvingInfoStatus);
				timeLastUpdate = System.currentTimeMillis();
				calculateSolvingTime();
				break;
			case COMPLETED:
				timeLastUpdate = TIME_COUNTING_STOPPED;
				timeSolvingStop = System.currentTimeMillis();
				calculateSolvingTime();
				SolverBase.saveSolvingDataToFile("=== status: " + oldStatus + " >>> " + solvingInfoStatus);
				infoChanged = true;
				break;
			case ITERATION_COMPLETED:
				timeLastUpdate = System.currentTimeMillis();
				SolverBase.saveSolvingDataToFile("=== status: " + oldStatus + " >>> " + solvingInfoStatus);
				setStatusTo(SolvingStatusEnum.IN_PROGRESS);
				infoChanged = true;
				break;
			case FAILED:
				timeLastUpdate = TIME_COUNTING_STOPPED;
				timeSolvingStop = System.currentTimeMillis();
				calculateSolvingTime();
				SolverBase.saveSolvingDataToFile("=== status: " + oldStatus + " >>> " + solvingInfoStatus);
				infoChanged = true;
				break;
			case STOPPED_BY_USER:
				timeLastUpdate = TIME_COUNTING_STOPPED;
				timeSolvingStop = System.currentTimeMillis();
				calculateSolvingTime();
				SolverBase.saveSolvingDataToFile("=== status: " + oldStatus + " >>> " + solvingInfoStatus);
				infoChanged = true;
				break;
			case STOPPED_BY_TIMEOUT:
				timeLastUpdate = TIME_COUNTING_STOPPED;
				timeSolvingStop = System.currentTimeMillis();
				calculateSolvingTime();
				SolverBase.saveSolvingDataToFile("=== status: " + oldStatus + " >>> " + solvingInfoStatus);
				infoChanged = true;
				break;
			case STOPPED_BY_GENERATIONS_LIMIT:
				timeLastUpdate = TIME_COUNTING_STOPPED;
				timeSolvingStop = System.currentTimeMillis();
				calculateSolvingTime();
				SolverBase.saveSolvingDataToFile("=== status: " + oldStatus + " >>> " + solvingInfoStatus);
				infoChanged = true;
				break;
			case ERROR:
				SolverBase.saveSolvingDataToFile("=== status: " + oldStatus + " >>> " + solvingInfoStatus);
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

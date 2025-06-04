public class SolverStarter {
	private static final int GENERATIONS_NUMBER_MIN = 5_000;
	private static final int GENERATIONS_NUMBER_MAX = 500_000;
	private static final int GENERATIONS_NUMBER_STEP = 5_000;
	private static final int GENERATIONS_NUMBER_DEFAULT = 50_000;
	private static int generationsNumber;

	private static SudokuBoardsEnum sudokuTestBoard;
	private static SolvingMethodsEnum solvingMethod;
	private static boolean calculatingIsOn;
	public static Board boardForScreen;

	/**
	 * ==============================================================================
	 * =================================================================MET.STATYCZNE
	 * ==============================================================================
	 */

	static {
		sudokuTestBoard = SudokuBoardsEnum.X3N;
		solvingMethod = SolvingMethodsEnum.GENETIC_X01;
		generationsNumber = GENERATIONS_NUMBER_DEFAULT;
		calculatingIsOn = false;
		boardForScreen = new Board(SolverStarter.sudokuTestBoard.getBoardData());
	}

	public static String getSudokuTestBoardName() {
		return sudokuTestBoard.getDisplayName();
	}

	public static SudokuBoardsEnum changeSudokuTestBoard() {
		sudokuTestBoard = sudokuTestBoard.next();
		boardForScreen = new Board(sudokuTestBoard.getBoardData());
		return sudokuTestBoard;
	}

	public static String getSolvingMethodName() {
		return solvingMethod.getDisplayName();
	}

	public static SolvingMethodsEnum changeSolvingMethod() {
		return solvingMethod = solvingMethod.next();
	}

	public static int getGenerationsNumber() {
		return generationsNumber;
	}

	public static void changeGenerationsNumberByStep() {
		generationsNumber += GENERATIONS_NUMBER_STEP;
		if (generationsNumber > GENERATIONS_NUMBER_MAX) {
			generationsNumber = GENERATIONS_NUMBER_MIN;
		}
	}

	public static boolean isCalculatingOn() {
		return calculatingIsOn;
	}

	public static void switchCalculatingOn() {
		calculatingIsOn = !calculatingIsOn;
	}

	/**
	 * ==============================================================================
	 * ================================================================MET.DYNAMICZNE
	 * ==============================================================================
	 */

	public static void startSolving() {
		// switch (solvingMethod) {
		// case value:

		// break;

		// default:
		// break;
		// }

	}

}

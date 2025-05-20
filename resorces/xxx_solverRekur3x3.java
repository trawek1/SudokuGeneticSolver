public class xxx_solverRekur3x3 {
	private static final int GRID_SIZE = 9;
	private static int startedRoads = 0;

	public static boolean solveSudoku(int[][] board) {
		for (int row = 0; row < GRID_SIZE; row++) {
			for (int col = 0; col < GRID_SIZE; col++) {
				if (board[row][col] == 0) {
					for (int number = 1; number <= GRID_SIZE; number++) {
						if (isValidPlacement(board, number, row, col)) {
							startedRoads++;

							board[row][col] = number;
							if (solveSudoku(board)) { // Rekurencyjnie próbuj rozwiązać resztę
								return true; // Znaleziono rozwiązanie
							} else {
								board[row][col] = 0; // Cofnij, jeśli ta ścieżka nie prowadzi do rozwiązania
							}
						}
					}
					return false; // Nie znaleziono poprawnej cyfry dla tego pola
				}
			}
		}
		return true; // Wszystkie pola wypełnione, Sudoku rozwiązane
	}

	private static boolean isValidPlacement(int[][] board, int number, int row, int col) {
		// Sprawdź wiersz
		for (int i = 0; i < GRID_SIZE; i++) {
			if (board[row][i] == number) {
				return false;
			}
		}

		// Sprawdź kolumnę
		for (int i = 0; i < GRID_SIZE; i++) {
			if (board[i][col] == number) {
				return false;
			}
		}

		// Sprawdź blok 3x3
		int boxRow = row - row % 3;
		int boxCol = col - col % 3;

		for (int i = boxRow; i < boxRow + 3; i++) {
			for (int j = boxCol; j < boxCol + 3; j++) {
				if (board[i][j] == number) {
					return false;
				}
			}
		}

		return true; // Liczba może być umieszczona w tym miejscu
	}

	public static void printBoard(int[][] board) {
		for (int row = 0; row < GRID_SIZE; row++) {
			if (row % 3 == 0 && row != 0) {
				System.out.println("-----------");
			}
			for (int col = 0; col < GRID_SIZE; col++) {
				if (col % 3 == 0 && col != 0) {
					System.out.print("|");
				}
				System.out.print(board[row][col] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int[][] board = {
				{ 5, 3, 0, 0, 7, 0, 0, 0, 0 },
				{ 6, 0, 0, 1, 9, 5, 0, 0, 0 },
				{ 0, 9, 8, 0, 0, 0, 0, 6, 0 },
				{ 8, 0, 0, 0, 6, 0, 0, 0, 3 },
				{ 4, 0, 0, 8, 0, 3, 0, 0, 1 },
				{ 7, 0, 0, 0, 2, 0, 0, 0, 6 },
				{ 0, 6, 0, 0, 0, 0, 2, 8, 0 },
				{ 0, 0, 0, 4, 1, 9, 0, 0, 5 },
				{ 0, 0, 0, 0, 8, 0, 0, 7, 9 }
		};

		// System.out.println("Plansza przed rozwiązaniem:");
		// printBoard(board);
		long startTime = System.nanoTime();
		boolean isSolved = solveSudoku(board);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime); // Czas w nanosekundach
		double durationMillis = (double) duration / 1_000_000.0;
		if (isSolved) {
			System.out.println("\nSudoku rozwiązane.");
			// System.out.println("\nPlansza po rozwiązaniu:");
			// printBoard(board);
		} else {
			System.out.println("\nBrak rozwiązania.");
		}
		System.out.println("Czas wykonania (nanosekundy): " + duration);
		System.out.println("Czas wykonania (milisekundy): " + durationMillis);
		System.out.println("Ścieżki sprawdzone: " + startedRoads);
	}
}
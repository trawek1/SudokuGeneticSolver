public class xxx_solverRekur4x4 {
	private static final int GRID_SIZE = 16;
	private static final int BLOCK_SIZE = 4;

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
		int boxRow = row - row % BLOCK_SIZE;
		int boxCol = col - col % BLOCK_SIZE;

		for (int i = boxRow; i < boxRow + BLOCK_SIZE; i++) {
			for (int j = boxCol; j < boxCol + BLOCK_SIZE; j++) {
				if (board[i][j] == number) {
					return false;
				}
			}
		}

		return true; // Liczba może być umieszczona w tym miejscu
	}

	public static void printBoard(int[][] board) {
		for (int row = 0; row < GRID_SIZE; row++) {
			if (row % BLOCK_SIZE == 0 && row != 0) {
				System.out.println("-----------");
			}
			for (int col = 0; col < GRID_SIZE; col++) {
				if (col % BLOCK_SIZE == 0 && col != 0) {
					System.out.print("|");
				}
				System.out.print(board[row][col] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int[][] board = {
				{ 0, 12, 0, 0, 0, 0, 0, 8, 0, 0, 0, 1, 0, 0, 0, 0 },
				{ 6, 0, 0, 0, 0, 4, 1, 14, 0, 12, 0, 3, 0, 0, 0, 0 },
				{ 0, 14, 0, 7, 15, 13, 12, 5, 0, 0, 0, 0, 0, 0, 0, 9 },
				{ 0, 9, 2, 5, 10, 7, 0, 0, 8, 0, 4, 0, 0, 0, 12, 6 },
				{ 0, 0, 11, 0, 5, 10, 0, 0, 2, 1, 3, 0, 16, 0, 14, 0 },
				{ 0, 0, 0, 0, 8, 0, 4, 15, 13, 5, 14, 0, 7, 11, 2, 10 },
				{ 0, 0, 8, 0, 16, 0, 14, 12, 6, 0, 11, 0, 13, 0, 0, 15 },
				{ 0, 4, 0, 13, 3, 2, 0, 0, 0, 16, 0, 10, 0, 1, 5, 0 },
				{ 0, 11, 7, 16, 0, 0, 0, 0, 1, 0, 0, 9, 4, 10, 15, 13 },
				{ 15, 8, 6, 0, 7, 16, 0, 0, 0, 14, 0, 0, 0, 0, 3, 0 },
				{ 12, 0, 0, 0, 4, 15, 0, 13, 11, 7, 16, 0, 0, 9, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 6, 0, 0, 8, 0 },
				{ 3, 0, 0, 6, 12, 5, 15, 0, 4, 0, 10, 0, 0, 0, 0, 2 },
				{ 8, 0, 9, 11, 0, 0, 7, 0, 0, 6, 0, 16, 0, 13, 0, 0 },
				{ 4, 0, 14, 0, 6, 0, 0, 0, 7, 0, 0, 0, 0, 0, 1, 0 },
				{ 0, 0, 5, 12, 0, 0, 0, 4, 3, 9, 1, 15, 0, 0, 7, 14 }
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
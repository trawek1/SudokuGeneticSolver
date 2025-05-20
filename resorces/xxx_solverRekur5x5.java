public class xxx_solverRekur5x5 {
	private static final int GRID_SIZE = 25;
	private static final int BLOCK_SIZE = 5;

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
				{ 0, 1, 0, 0, 7, 0, 0, 10, 0, 0, 4, 13, 0, 25, 0, 20, 0, 0, 0, 0, 9, 17, 11, 8, 0 },
				{ 15, 0, 0, 0, 0, 20, 0, 6, 11, 0, 2, 1, 0, 0, 19, 0, 0, 0, 8, 0, 0, 0, 7, 21, 0 },
				{ 0, 22, 0, 0, 13, 0, 1, 17, 0, 0, 0, 24, 0, 9, 3, 11, 0, 19, 0, 0, 0, 0, 4, 5, 20 },
				{ 6, 0, 21, 0, 20, 0, 24, 0, 0, 0, 11, 0, 0, 0, 10, 0, 0, 0, 9, 7, 12, 0, 14, 0, 1 },
				{ 0, 0, 4, 24, 0, 7, 9, 2, 0, 0, 0, 0, 0, 18, 0, 0, 0, 22, 13, 0, 6, 0, 0, 0, 0 },
				{ 0, 0, 0, 4, 0, 9, 0, 0, 17, 25, 0, 8, 16, 22, 0, 2, 0, 0, 0, 0, 0, 20, 23, 0, 24 },
				{ 0, 15, 9, 0, 0, 23, 19, 20, 0, 16, 0, 0, 0, 4, 21, 0, 0, 0, 18, 0, 0, 0, 2, 13, 6 },
				{ 0, 10, 1, 0, 18, 12, 0, 11, 14, 0, 0, 7, 0, 0, 0, 21, 0, 0, 25, 20, 22, 4, 0, 9, 8 },
				{ 0, 24, 25, 0, 0, 22, 0, 13, 0, 0, 0, 12, 18, 0, 0, 23, 15, 0, 17, 0, 11, 0, 0, 16, 0 },
				{ 0, 0, 0, 0, 11, 0, 5, 0, 0, 0, 9, 15, 0, 0, 0, 14, 0, 13, 1, 0, 0, 25, 0, 3, 0 },
				{ 0, 0, 0, 12, 0, 0, 0, 0, 0, 9, 0, 18, 0, 8, 16, 0, 23, 0, 14, 0, 0, 1, 0, 0, 2 },
				{ 0, 2, 0, 0, 10, 0, 4, 15, 0, 13, 3, 0, 0, 19, 0, 0, 0, 0, 16, 0, 0, 9, 0, 7, 5 },
				{ 22, 0, 0, 0, 0, 0, 20, 14, 23, 10, 5, 0, 17, 6, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0 },
				{ 0, 25, 18, 0, 8, 0, 17, 0, 22, 0, 0, 4, 0, 0, 1, 0, 0, 3, 0, 0, 0, 0, 0, 0, 16 },
				{ 0, 3, 0, 13, 4, 19, 0, 0, 0, 18, 0, 14, 11, 0, 24, 0, 0, 0, 15, 0, 25, 10, 0, 0, 0 },
				{ 0, 0, 7, 1, 0, 11, 0, 0, 13, 0, 0, 25, 0, 17, 0, 15, 0, 0, 0, 0, 18, 0, 0, 0, 14 },
				{ 0, 0, 0, 0, 0, 0, 23, 0, 25, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 2, 16, 11, 21, 0, 12 },
				{ 13, 16, 0, 0, 0, 5, 0, 4, 15, 12, 0, 0, 0, 21, 14, 0, 6, 23, 0, 3, 2, 7, 25, 0, 0 },
				{ 11, 14, 0, 0, 0, 0, 21, 22, 24, 0, 0, 3, 0, 16, 12, 0, 0, 20, 19, 0, 0, 5, 0, 0, 0 },
				{ 0, 0, 10, 15, 21, 2, 0, 0, 0, 17, 0, 0, 0, 0, 0, 18, 16, 0, 0, 0, 13, 3, 0, 6, 0 },
				{ 0, 18, 12, 0, 0, 0, 0, 0, 2, 11, 24, 21, 14, 0, 0, 0, 0, 15, 23, 0, 0, 0, 0, 22, 0 },
				{ 23, 13, 0, 5, 0, 17, 0, 16, 0, 0, 6, 0, 25, 0, 15, 0, 11, 0, 0, 0, 0, 14, 19, 18, 3 },
				{ 0, 0, 0, 20, 0, 0, 22, 0, 10, 24, 0, 0, 1, 12, 0, 0, 0, 8, 0, 16, 0, 0, 17, 0, 9 },
				{ 0, 4, 0, 25, 14, 8, 0, 0, 0, 6, 0, 0, 0, 0, 13, 7, 2, 0, 20, 21, 0, 0, 0, 12, 15 },
				{ 10, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 9, 0, 17, 0, 0, 0, 0, 25, 0, 0, 13, 11, 0 }
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
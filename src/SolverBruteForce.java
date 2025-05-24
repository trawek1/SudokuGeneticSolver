import java.util.Set;

import org.tinylog.Logger;

public class SolverBruteForce extends SolverBase {
    private Board board;
    private int[] boardData;

    public SolverBruteForce(int[] _boardData) {
        try {
            this.board = new Board(_boardData);
            this.boardData = _boardData;
        } catch (IllegalArgumentException e) {
            Logger.error("Niepoprawne dane planszy! {}", e.getMessage());
            throw new IllegalArgumentException("Niepoprawne dane planszy!");
        }
    }

    private void resetBoard() {
        this.board = new Board(this.boardData);
    }

    private boolean isValuePossibleInField(int _row, int _col, int _value) {
        Set<Integer> values;

        values = board.getUsedValuesFromRow(_row);
        if (values.contains(_value)) {
            return false;
        }
        values = board.getUsedValuesFromCol(_col);
        if (values.contains(_value)) {
            return false;
        }
        values = board.getUsedValuesFromBlock(_row, _col);
        if (values.contains(_value)) {
            return false;
        }

        return true;
    }

    private boolean solve() {
        for (int row = 1; row <= board.getBoardSize(); row++) {
            for (int col = 1; col <= board.getBoardSize(); col++) {
                if (board.isConstField(row, col)) {
                    continue;
                }
                if (board.getValue(row, col) == BoardBase.EMPTY_FIELD) {
                    for (int value = 1; value <= board.getMaxValue(); value++) {
                        if (isValuePossibleInField(row, col, value)) {
                            board.setValue(row, col, value);
                            if (solve()) {
                                return true;
                            } else {
                                board.removeValue(row, col);
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public void startSolving() {
        this.board.xxx_showBoard();
        Logger.info("Rozpoczeto rozwiązywanie...");

        this.startTimeMeasurement();
        boolean isSolved = this.solve();
        this.stopTimeMeasurement();

        if (isSolved) {
            Logger.info("Znaleziono rozwiązanie!");
        } else {
            Logger.info("Nie znaleziono rozwiązania!");
        }
        Logger.info("Czas rozwiązywania: {}", this.showSolvingTime());
        this.board.xxx_showBoard();
    }

    public void startSolvingManyTimes() {
        this.setSolvingIterationsCount(3);
        this.resetTimeMeasurement();
        Logger.info("Rozpoczeto rozwiązywanie wielokrotne...");
        for (int i = 0; i < this.getSolvingIterationsCount(); i++) {
            this.resetBoard();
            this.startTimeMeasurement();
            boolean isSolved = this.solve();
            this.stopTimeMeasurement();
            Logger.info("Rozwiązywanie nr {}, wynik {}, czas {}", i + 1, isSolved ? "udane" : "błędne",
                    this.showSolvingTime());
        }
        Logger.info("Średni czas rozwiązywania: {}", this.showSolvingAverageTime());
        Logger.info("Zakończono rozwiązywanie wielokrotne...");
    }
}

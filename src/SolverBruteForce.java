import java.time.LocalDateTime;
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

    public void resetBoard() {
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
        String info;
        for (int row = 1; row <= board.getBoardSize(); row++) {
            if (SolverInfo.getStatus() != SolvingStatusEnum.IN_PROGRESS) {
                break;
            }
            for (int col = 1; col <= board.getBoardSize(); col++) {
                if (SolverInfo.getStatus() != SolvingStatusEnum.IN_PROGRESS) {
                    break;
                }
                if (board.isConstField(row, col)) {
                    continue;
                }
                if (board.getValue(row, col) == BoardBase.EMPTY_FIELD) {
                    for (int value = 1; value <= board.getMaxValue(); value++) {
                        if (SolverInfo.getStatus() != SolvingStatusEnum.IN_PROGRESS) {
                            break;
                        }

                        if (isValuePossibleInField(row, col, value)) {
                            info = String.format("wie=%2d, kol=%2d, war=%2d", row, col, value);
                            SolverInfo.addDetails(info);
                            SolverBase.saveSolvingDataToFile("ustawiam," + info);
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
        SolverBase.saveSolvingDataToFile("=== start   : " + LocalDateTime.now());
        startTimeMeasurement();
        boolean isSolved = this.solve();
        stopTimeMeasurement();
        SolverBase.saveSolvingDataToFile("=== stop    : " + LocalDateTime.now());

        if (SolverInfo.getStatus() == SolvingStatusEnum.IN_PROGRESS) {
            if (isSolved) {
                SolverInfo.changeStatusTo(SolvingStatusEnum.COMPLETED);
                SolverBase.saveSolvingDataToFile("=== czas rozwiązania: " + showSolvingAverageTime());
            } else {
                SolverInfo.changeStatusTo(SolvingStatusEnum.FAILED);
            }
        }
    }

    public void startSolvingManyTimes(int _solvingNumber) {
        // if (!setSolvingIterationsCount(_solvingNumber)) {
        // Logger.warn("Nieoczekiwanie przerwano rozwiązywanie sudoku!");
        // return;
        // }

        // resetTimeMeasurement();
        // for (int i = 0; i < getSolvingIterationsCount(); i++) {
        // if (SolverInfo.getStatus() != SolvingStatusEnum.IN_PROGRESS) {
        // return;
        // }

        // resetBoard();

        // // startTimeMeasurement();
        // boolean isSolved = this.solve();
        // // stopTimeMeasurement();

        // Logger.info("Rozwiązywanie nr {}, wynik {}, czas {}", i + 1, isSolved ?
        // "udane" : "błędne",
        // showSolvingTime());
        // }
        // Logger.info("Średni czas rozwiązywania: {}", showSolvingAverageTime());
    }
}

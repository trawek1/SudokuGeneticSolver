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

    public void saveToFileMethodParameters() {
        SolverBase.saveSolvingDataToFile("=== metoda  : " + SudokuSolverGUI.getSolvingMethodName());
        SolverBase.saveSolvingDataToFile("=== plansza : " + SudokuSolverGUI.getSudokuTestBoardName());
        SolverBase.saveSolvingDataToFile("=== czas max: " + convertMilisecondsToHMSV(SOLVING_TIME_MAX, 2));
        SolverBase.saveSolvingDataToFile("=== iteracje: " + SolverBase.getSolvingIterationsLimit());
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
                            info = String.format("ite=%2d, wie=%2d, kol=%2d, war=%2d",
                                    getSolvingIterationsCounter(), row, col, value);
                            SolverInfo.addDetails(info);
                            SolverBase.saveSolvingDataToFile("dane, " + info);
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
                if (SolverBase.areIterationsInProgress()) {
                    SolverInfo.setStatusTo(SolvingStatusEnum.ITERATION_COMPLETED);
                } else {
                    SolverInfo.setStatusTo(SolvingStatusEnum.COMPLETED);
                }
                SolverBase.saveSolvingDataToFile("=== czas rozwiazania: " + showSolvingAverageTime());
            } else {
                SolverInfo.setStatusTo(SolvingStatusEnum.FAILED);
            }
        }
    }
}

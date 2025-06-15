import org.tinylog.Logger;

import java.util.HashSet;
import java.util.Set;

public class SolverGeneticIndividual extends SolverBase {
    private static final int FITNESS_PENALTY_EMPTY_FIELD = 1;
    private static final int FITNESS_PENALTY_VALUE_COLLISION = 2;
    private static final int FITNESS_PENALTY_CONST_COLLISION = 5;

    private static final FitnessCalculatingMethodsEnum FITNESS_CALCULATING_METHOD_DEFAULT = FitnessCalculatingMethodsEnum.VALUES_COLLISIONS;
    private static FitnessCalculatingMethodsEnum fitnessCalculatingMethod;

    private Board board;
    private int[] initialBoardData;
    private int fitnessLevel;

    /**
     * ==============================================================================
     * =================================================================MET.STATYCZNE
     * ==============================================================================
     */

    static {
        fitnessCalculatingMethod = FITNESS_CALCULATING_METHOD_DEFAULT;
    }

    public static String getFitnessCalculatingMethodName() {
        return fitnessCalculatingMethod.getDisplayName();
    }

    public static FitnessCalculatingMethodsEnum changeFitnessCalculatingMethod() {
        return fitnessCalculatingMethod = fitnessCalculatingMethod.next();
    }

    public static void saveSolvingPreferencesToFile() {
        saveSolvingDataToFile("Metoda obliczania dopasowania: " + fitnessCalculatingMethod);
    }

    /**
     * ==============================================================================
     * ================================================================MET.DYNAMICZNE
     * ==============================================================================
     */

    public SolverGeneticIndividual(int[] _boardData) {
        this.board = new Board(_boardData);
        this.initialBoardData = _boardData;
        this.fitnessLevel = 0;
    }

    public void getBoardFromParent() {
        SolverGeneticParentMaker parent = new SolverGeneticParentMaker(this.initialBoardData, true);
        this.board = new Board(parent.getBoardData());
        this.calculateFitness();
    }

    public int getFitnessLevel() {
        return fitnessLevel;
    }

    public void calculateFitness() {
        switch (fitnessCalculatingMethod) {
            case MISSING_VALUES:
                this.calculateFitnessByMissingValues();
                break;
            case VALUES_COLLISIONS:
                this.calculateFitnessByValuesCollisions();
                break;
            default:
                Logger.error("Nieznana metoda liczenia dopasowania: {}", fitnessCalculatingMethod);
                this.calculateFitnessByValuesCollisions();
        }
        // if (this.fitnessLevel == 0) {
        // SolverBase.stopTimeMeasurement();
        // Logger.info("Średni czas rozwiązywania: {}", showSolvingAverageTime());
        // }
    }

    public void calculateFitnessByMissingValues() {
        this.fitnessLevel = 0;

        for (int i = 1; i <= board.getBoardSize(); i++) {
            Set<Integer> rowValues = new HashSet<>(board.getUsedValuesFromRow(i));
            this.fitnessLevel += this.board.getMaxValue() - rowValues.size();

            Set<Integer> colValues = new HashSet<>(board.getUsedValuesFromCol(i));
            this.fitnessLevel += this.board.getMaxValue() - colValues.size();
        }

        for (int row = 1; row < board.getBoardSize(); row += board.getSudokuSize()) {
            for (int col = 1; col < board.getBoardSize(); col += board.getSudokuSize()) {
                Set<Integer> blockValues = new HashSet<>(board.getUsedValuesFromBlock(row, col));
                this.fitnessLevel += this.board.getMaxValue() - blockValues.size();
            }
        }
    }

    public void calculateFitnessByValuesCollisions() {
        this.fitnessLevel = 0;

        for (int row = 1; row <= board.getBoardSize(); row++) {
            for (int col = 1; col <= board.getBoardSize(); col++) {
                int value = board.getValue(row, col);

                if (value == BoardBase.EMPTY_FIELD) {
                    this.fitnessLevel += FITNESS_PENALTY_EMPTY_FIELD;
                }

                for (int checkInRow = 1; checkInRow <= board.getBoardSize(); checkInRow++) {
                    if (checkInRow == row) {
                        continue;
                    } else if (value == board.getValue(checkInRow, col)) {
                        this.fitnessLevel += FITNESS_PENALTY_VALUE_COLLISION;
                        if (board.isConstField(checkInRow, col)) {
                            this.fitnessLevel += FITNESS_PENALTY_CONST_COLLISION;
                        }
                    }
                }

                for (int checkInCol = 1; checkInCol <= board.getBoardSize(); checkInCol++) {
                    if (checkInCol == col) {
                        continue;
                    } else if (value == board.getValue(row, checkInCol)) {
                        this.fitnessLevel += FITNESS_PENALTY_VALUE_COLLISION;
                        if (board.isConstField(row, checkInCol)) {
                            this.fitnessLevel += FITNESS_PENALTY_CONST_COLLISION;
                        }
                    }
                }

                int blockRowStart = ((row - 1) / board.getSudokuSize()) * board.getSudokuSize() + 1;
                int blockRowEnd = blockRowStart + board.getSudokuSize() - 1;
                int blockColStart = ((col - 1) / board.getSudokuSize()) * board.getSudokuSize() + 1;
                int blockColEnd = blockColStart + board.getSudokuSize() - 1;
                for (int checkInRow = blockRowStart; checkInRow <= blockRowEnd; checkInRow++) {
                    for (int checkInCol = blockColStart; checkInCol <= blockColEnd; checkInCol++) {
                        if (checkInRow == row && checkInCol == col) {
                            continue;
                        } else if (value == board.getValue(checkInRow, checkInCol)) {
                            this.fitnessLevel += FITNESS_PENALTY_VALUE_COLLISION;
                            if (board.isConstField(checkInRow, checkInCol)) {
                                this.fitnessLevel += FITNESS_PENALTY_CONST_COLLISION;
                            }
                        }
                    }
                }

            }
        }
    }

    public int[] getInitialBoardData() {
        return this.initialBoardData;
    }

    public int[] getBoardData() {
        return this.board.getBoardData();
    }

    public void xxx_showBoard() {
        this.board.xxx_showBoard();
    }

}
import org.tinylog.Logger;

import java.util.HashSet;
import java.util.Set;

public class SolverGeneticIndividual extends SolverBase {
    private static final int FITNESS_PENALTY_EMPTY_FIELD = 1;
    private static final int FITNESS_PENALTY_VALUE_COLLISION = 2;
    private static final int FITNESS_PENALTY_CONST_COLLISION = 5;

    private static final METHODS_OF_PARENT_GENERATING PARENT_GENERATING_METHOD_DEFAULT = METHODS_OF_PARENT_GENERATING.RANDOM_WITH_CHECKING;
    private static METHODS_OF_PARENT_GENERATING parentGeneratingMethod;

    private static final METHODS_OF_FITNESS_CALCULATING FITNESS_CALCULATING_METHOD_DEFAULT = METHODS_OF_FITNESS_CALCULATING.VALUES_COLLISIONS;
    private static METHODS_OF_FITNESS_CALCULATING fitnessCalculatingMethod;

    private Board board;
    private int[] initialBoardData;
    private int boardErrorLevel;

    /**
     * ==============================================================================
     * =================================================================MET.STATYCZNE
     * ==============================================================================
     */

    static {
        parentGeneratingMethod = PARENT_GENERATING_METHOD_DEFAULT;
        fitnessCalculatingMethod = FITNESS_CALCULATING_METHOD_DEFAULT;
    }

    public static void saveSolvingPreferencesToFile() {
        saveSolvingDataToFile("Metoda generowania rodziców: " + parentGeneratingMethod);
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
        this.boardErrorLevel = 0;
    }

    public void getBoardFromParent() {
        SolverGeneticParentMaker parent = new SolverGeneticParentMaker(this.initialBoardData);
        switch (parentGeneratingMethod) {
            case METHODS_OF_PARENT_GENERATING.RANDOM_FULL:
                parent.generatorRandomFull();
                break;
            case METHODS_OF_PARENT_GENERATING.RANDOM_WITH_CHECKING:
                parent.generatorRandomWithChecking();
                break;
            default:
                Logger.error("Nieznana metoda generowania rodziców: {}", parentGeneratingMethod);
                parent.generatorRandomWithChecking();
        }
        this.board = null;
        this.board = new Board(parent.getBoardData());
        this.calculateFitness();
    }

    public int getBoardErrorLevel() {
        return boardErrorLevel;
    }

    public void calculateFitness() {
        switch (fitnessCalculatingMethod) {
            case VALUES_MISSING:
                this.calculateFitnessByMissingValues();
                break;
            case VALUES_COLLISIONS:
                this.calculateFitnessByValuesCollisions();
                break;
            default:
                Logger.error("Nieznana metoda liczenia dopasowania: {}", fitnessCalculatingMethod);
                this.calculateFitnessByValuesCollisions();
        }
    }

    public void calculateFitnessByMissingValues() {
        this.boardErrorLevel = 0;

        for (int i = 1; i <= board.getBoardSize(); i++) {
            Set<Integer> rowValues = new HashSet<>(board.getUsedValuesFromRow(i));
            this.boardErrorLevel += this.board.getMaxValue() - rowValues.size();

            Set<Integer> colValues = new HashSet<>(board.getUsedValuesFromCol(i));
            this.boardErrorLevel += this.board.getMaxValue() - colValues.size();
        }

        for (int row = 1; row < board.getBoardSize(); row += board.getSudokuSize()) {
            for (int col = 1; col < board.getBoardSize(); col += board.getSudokuSize()) {
                Set<Integer> blockValues = new HashSet<>(board.getUsedValuesFromBlock(row, col));
                this.boardErrorLevel += this.board.getMaxValue() - blockValues.size();
            }
        }
    }

    public void calculateFitnessByValuesCollisions() {
        this.boardErrorLevel = 0;

        for (int row = 1; row <= board.getBoardSize(); row++) {
            for (int col = 1; col <= board.getBoardSize(); col++) {
                int value = board.getValue(row, col);

                if (value == BoardBase.EMPTY_FIELD) {
                    this.boardErrorLevel += FITNESS_PENALTY_EMPTY_FIELD;
                }

                for (int checkInRow = 1; checkInRow <= board.getBoardSize(); checkInRow++) {
                    if (checkInRow == row) {
                        continue;
                    } else if (value == board.getValue(checkInRow, col)) {
                        this.boardErrorLevel += FITNESS_PENALTY_VALUE_COLLISION;
                        if (board.isConstField(checkInRow, col)) {
                            this.boardErrorLevel += FITNESS_PENALTY_CONST_COLLISION;
                        }
                    }
                }

                for (int checkInCol = 1; checkInCol <= board.getBoardSize(); checkInCol++) {
                    if (checkInCol == col) {
                        continue;
                    } else if (value == board.getValue(row, checkInCol)) {
                        this.boardErrorLevel += FITNESS_PENALTY_VALUE_COLLISION;
                        if (board.isConstField(row, checkInCol)) {
                            this.boardErrorLevel += FITNESS_PENALTY_CONST_COLLISION;
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
                            this.boardErrorLevel += FITNESS_PENALTY_VALUE_COLLISION;
                            if (board.isConstField(checkInRow, checkInCol)) {
                                this.boardErrorLevel += FITNESS_PENALTY_CONST_COLLISION;
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
import org.tinylog.Logger;

import java.util.Random;
import java.util.Set;

public class SolverGeneticParentMaker extends SolverBase {
    private static final ParentGeneratingMethods PARENT_GENERATING_METHOD_DEFAULT = ParentGeneratingMethods.RANDOM_WITH_CHECKING;
    private static ParentGeneratingMethods parentGeneratingMethod;

    private final int MAX_SAFE_GENERATION_ATTEMPTS = 25;
    private Board board;
    private final Random random = new Random();

    /**
     * ==============================================================================
     * =================================================================MET.STATYCZNE
     * ==============================================================================
     */

    static {
        parentGeneratingMethod = PARENT_GENERATING_METHOD_DEFAULT;
    }

    public static String getParentGeneratingMethodName() {
        return parentGeneratingMethod.getDisplayName();
    }

    public static void changeParentGeneratingMethod() {
        parentGeneratingMethod = parentGeneratingMethod.next();
    }

    public static void saveSolvingPreferencesToFile() {
        saveSolvingDataToFile("Metoda generowania rodziców: " + parentGeneratingMethod);
    }

    /**
     * ==============================================================================
     * ================================================================MET.DYNAMICZNE
     * ==============================================================================
     */

    public SolverGeneticParentMaker(int[] _boardData, boolean _fillEmptyFields) {
        super();
        this.board = new Board(_boardData);
        if (_fillEmptyFields) {
            this.generateParent();
        }
    }

    public void generateParent() {
        switch (parentGeneratingMethod) {
            case ParentGeneratingMethods.RANDOM_FULL -> this.generatorRandomFull();
            case ParentGeneratingMethods.RANDOM_WITH_CHECKING -> this.generatorRandomWithChecking();
            default -> {
                Logger.error("Nieznana metoda generowania rodziców: {}", parentGeneratingMethod);
                this.generatorRandomWithChecking();
            }
        }
    }

    public void generatorRandomFull() {
        for (int row = 1; row <= this.board.getBoardSize(); row++) {
            for (int col = 1; col <= this.board.getBoardSize(); col++) {
                if (board.isEmptyField(row, col)) {
                    board.setValue(row, col, random.nextInt(this.board.getBoardSize()) + 1);
                }
            }
        }
    }

    public void generatorRandomWithChecking() {
        for (int row = 1; row <= this.board.getBoardSize(); row++) {
            for (int col = 1; col <= this.board.getBoardSize(); col++) {
                if (board.isEmptyField(row, col)) {
                    Set<Integer> rowValues = board.getUsedValuesFromRow(row);
                    Set<Integer> colValues = board.getUsedValuesFromCol(col);
                    Set<Integer> blockValues = board.getUsedValuesFromBlock(row, col);
                    boolean isFieldStillEmpty = true;
                    for (int attempt = 0; attempt < MAX_SAFE_GENERATION_ATTEMPTS; attempt++) {
                        int value = random.nextInt(this.board.getBoardSize()) + 1;
                        if (!rowValues.contains(value) && !colValues.contains(value) && !blockValues.contains(value)) {
                            board.setValue(row, col, value);
                            isFieldStillEmpty = false;
                            break;
                        }
                    }
                    if (isFieldStillEmpty) {
                        board.setValue(row, col, random.nextInt(this.board.getBoardSize()) + 1);
                    }
                }
            }
        }
    }

    public int[] getBoardData() {
        return this.board.getBoardData();
    }

    public void xxx_showBoard() {
        this.board.xxx_showBoard();
    }
}
// import org.tinylog.Logger;

import java.util.Random;
import java.util.Set;

public class SolverGeneticParentMaker extends SolverGeneticBase {
    private final int MAX_SAFE_GENERATION_ATTEMPTS = 25;
    private Board board;
    private final Random random = new Random();

    public SolverGeneticParentMaker(int[] _boardData) {
        this.board = new Board(_boardData);
    }

    public void hardRandom() {
        for (int row = 1; row <= this.board.getBoardSize(); row++) {
            for (int col = 1; col <= this.board.getBoardSize(); col++) {
                if (board.isEmptyField(row, col)) {
                    board.setValue(row, col, random.nextInt(this.board.getBoardSize()) + 1);
                }
            }
        }
    }

    public void softRandom() {
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
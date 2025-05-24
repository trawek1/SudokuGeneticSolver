// import org.tinylog.Logger;

import java.util.HashSet;
import java.util.Set;

public class SolverGeneticIndividual {
    private Board board;
    private int[] boardData;
    private int boardErrorLevel; // TODO > zmienić na errorLevel

    public SolverGeneticIndividual(int[] _boardData) {
        this.board = new Board(_boardData);
        this.boardData = _boardData;
        this.boardErrorLevel = 0;
    }

    public void getBoardFromParentHard() {
        SolverGeneticParentMaker parent = new SolverGeneticParentMaker(this.boardData);
        parent.hardRandom();
        this.board = null;
        this.board = new Board(parent.getBoardData());
        this.boardErrorLevel = 0;
    }

    public void getBoardFromParentSoft() {
        SolverGeneticParentMaker parent = new SolverGeneticParentMaker(this.boardData);
        parent.softRandom();
        this.board = null;
        this.board = new Board(parent.getBoardData());
        this.boardErrorLevel = 0;
    }

    public int getBoardErrorLevel() {
        return boardErrorLevel;
    }

    public void calculateFitness() {
        // TODO >> ta ocena nie uwzględnia powtórzen wartości, można to ulepszyć
        this.boardErrorLevel = 0;

        for (int i = 1; i <= board.getBoardSize(); i++) {
            Set<Integer> rowValues = new HashSet<>(board.getUsedValuesFromRow(i));
            this.boardErrorLevel += this.board.getMaxValue() - rowValues.size();

            Set<Integer> colValues = new HashSet<>(board.getUsedValuesFromCol(i));
            this.boardErrorLevel += this.board.getMaxValue() - colValues.size();

            // Logger.debug("Oceny: wiersza[{}}]={}, kolumny[{}]={}, sumaryczna={}",
            // i, rowValues.size(), i, colValues.size(), this.fitness);
        }

        for (int row = 1; row < board.getBoardSize(); row += board.getSudokuSize()) {
            for (int col = 1; col < board.getBoardSize(); col += board.getSudokuSize()) {
                Set<Integer> blockValues = new HashSet<>(board.getUsedValuesFromBlock(row, col));
                this.boardErrorLevel += this.board.getMaxValue() - blockValues.size();
                // Logger.debug("Oceny: block[{},{}]={}, sumaryczna={}",
                // row, col, blockValues.size(), this.fitness);
            }
        }
    }

    // Krzyżowanie z innym osobnikiem.
    // public SolverGeneticIndividual crossover(SolverGeneticIndividual otherParent)
    // {
    // SolverGeneticIndividual child = new
    // SolverGeneticIndividual(this.initialBoard); // Potomek startuje z kopią
    // planszy początkowej

    // Random random = new Random();
    // for (int row = 1; row <= board.getBoardSize(); row++) {
    // for (int col = 1; col <= board.getBoardSize(); col++) {
    // if (initialBoard.getValue(row, col) == 0) { // Krzyżujemy tylko puste pola
    // // Losowo wybieramy wartość od jednego z rodziców
    // child.board.setValue(row, col, random.nextBoolean() ?
    // this.board.getValue(row, col) : otherParent.getBoard().getValue(row, col));
    // } else {
    // child.board.setValue(row, col, initialBoard.getValue(row, col)); //
    // Przepisujemy wartości początkowe
    // }
    // }
    // }

    // return child;
    // }

    // // Mutacja osobnika.
    // public void mutate() {
    // Random random = new Random();
    // for (int row = 1; row <= board.getBoardSize(); row++) {
    // for (int col = 1; col <= board.getBoardSize(); col++) {
    // if (initialBoard.getValue(row, col) == 0 && random.nextDouble() < 0.1) { //
    // 10% szansy na mutację
    // int newValue = random.nextInt(board.getBoardSize()) + 1;
    // board.setValue(row, col, newValue);
    // }
    // }
    // }
    // }

    // @Override
    // public String toString() {
    // StringBuilder sb = new StringBuilder();
    // sb.append("Fitness: ").append(fitness).append("\n");
    // int[] boardArray = board.exportToArray();
    // for (int i = 0; i < boardArray.length; i++) {
    // sb.append(String.format("%3d", boardArray[i]));
    // if ((i + 1) % board.getBoardSize() == 0) {
    // sb.append("\n");
    // }
    // }
    // return sb.toString();
    // }
    public void xxx_showBoard() {
        this.board.xxx_showBoard();
    }

}
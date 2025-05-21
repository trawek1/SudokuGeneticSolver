import org.tinylog.Logger;

public class BoardBase {
    public static final int RETURN_ERROR = -999999;
    public static final int EMPTY_FIELD = 0;
    public final int SUDOKU_SIZE_MIN = 2;
    public final int SUDOKU_SIZE_MAX = 6;
    public final int SUDOKU_SIZE_DEFAULT = 3;

    private final int sudokuSize;
    private final int boardSize;

    public BoardBase(int _size) {
        if (!this.isSudokuSizeInRange(_size)) {
            Logger.warn("Rozmiar sudoku jest spoza zakresu! Otrzymano {}, ustawiono {}.",
                    _size, SUDOKU_SIZE_DEFAULT);
            _size = SUDOKU_SIZE_DEFAULT;
        }
        this.sudokuSize = _size;
        this.boardSize = this.sudokuSize * this.sudokuSize;
    }

    private boolean isSudokuSizeInRange(int _size) {
        return _size > 0 && _size <= SUDOKU_SIZE_MAX;
    }

    public boolean isValueInRange(int _value) {
        return _value > 0 && _value <= this.boardSize;
    }

    public boolean isCorrectRowNumber(int _rowNumber) {
        return isValueInRange(_rowNumber);
    }

    public boolean isCorrectColNumber(int _colNumber) {
        return isValueInRange(_colNumber);
    }

    public boolean isCorrectBlockNumber(int _blockNumber) {
        return isValueInRange(_blockNumber);
    }

    public int getSudokuSize() {
        return this.sudokuSize;
    }

    public int getBoardSize() {
        return this.boardSize;
    }

    public int getMaxValue() {
        return this.boardSize;
    }
}

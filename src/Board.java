import org.tinylog.Logger;

import java.util.HashSet;
import java.util.Set;

public class Board extends BoardBase {
    private BoardField[][] board;

    public Board() {
        this(SUDOKU_SIZE_DEFAULT);
    }

    public Board(int _sudokuSize) {
        super(_sudokuSize);

        this.board = new BoardField[this.getBoardSize()][this.getBoardSize()];
        for (int row = 0; row < this.getBoardSize(); row++) {
            for (int col = 0; col < this.getBoardSize(); col++) {
                this.board[row][col] = new BoardField(_sudokuSize);
            }
        }
    }

    public Board(int[] _initialBoardData) {
        super((int) Math.sqrt(Math.sqrt(_initialBoardData.length)));
        this.setBoardData(_initialBoardData);
    }

    public void setBoardData(int[] _initialBoardData) {
        int boardSize = this.getBoardSize();
        if (_initialBoardData.length != boardSize * boardSize) {
            Logger.error("Tablica wartości planszy ma niepoprawny rozmiar! Otrzymano {} wartości pól.",
                    _initialBoardData.length);
            throw new IllegalArgumentException("Tablica wartości planszy ma niepoprawny rozmiar!");
        }

        this.board = new BoardField[this.getBoardSize()][this.getBoardSize()];
        int index = 0;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int fieldData = _initialBoardData[index++];
                if (fieldData < 0) {
                    this.board[row][col] = new BoardField(this.getSudokuSize(), Math.abs(fieldData));
                } else if (fieldData > 0) {
                    this.board[row][col] = new BoardField(this.getSudokuSize());
                    this.board[row][col].setValue(fieldData);
                } else {
                    this.board[row][col] = new BoardField(this.getSudokuSize());
                }
            }
        }
    }

    public boolean isEmptyField(int _rowNumber, int _colNumber) {
        if (!this.isCorrectRowNumber(_rowNumber)) {
            Logger.error("Numer wiersza jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _rowNumber);
            return false;
        }
        if (!this.isCorrectColNumber(_colNumber)) {
            Logger.error("Numer kolumny jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _colNumber);
            return false;
        }
        return this.board[_rowNumber - 1][_colNumber - 1].getValue() == EMPTY_FIELD;
    }

    public int getValue(int _rowNumber, int _colNumber) {
        if (!this.isCorrectRowNumber(_rowNumber)) {
            Logger.error("Numer wiersza jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _rowNumber);
            return RETURN_ERROR;
        }
        if (!this.isCorrectColNumber(_colNumber)) {
            Logger.error("Numer kolumny jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _colNumber);
            return RETURN_ERROR;
        }
        return this.board[_rowNumber - 1][_colNumber - 1].getValue();
    }

    public boolean setValue(int _rowNumber, int _colNumber, int _value) {
        if (!this.isCorrectRowNumber(_rowNumber)) {
            Logger.error("Numer wiersza jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _rowNumber);
            return false;
        }
        if (!this.isCorrectColNumber(_colNumber)) {
            Logger.error("Numer kolumny jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _colNumber);
            return false;
        }
        return this.board[_rowNumber - 1][_colNumber - 1].setValue(_value);
    }

    public boolean removeValue(int _rowNumber, int _colNumber) {
        if (!this.isCorrectRowNumber(_rowNumber)) {
            Logger.error("Numer wiersza jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _rowNumber);
            return false;
        }
        if (!this.isCorrectColNumber(_colNumber)) {
            Logger.error("Numer kolumny jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _colNumber);
            return false;
        }
        if (this.board[_rowNumber - 1][_colNumber - 1].isConstField()) {
            Logger.error("Nie można usunąć wartości z pola stałego!");
            return false;
        }
        return this.board[_rowNumber - 1][_colNumber - 1].removeValue();
    }

    public boolean isConstField(int _rowNumber, int _colNumber) {
        if (!this.isCorrectRowNumber(_rowNumber)) {
            Logger.error("Numer wiersza jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _rowNumber);
            return false;
        }
        if (!this.isCorrectColNumber(_colNumber)) {
            Logger.error("Numer kolumny jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _colNumber);
            return false;
        }
        return this.board[_rowNumber - 1][_colNumber - 1].isConstField();
    }

    public Set<Integer> getUsedValuesFromRow(int _rowNumber) {
        Set<Integer> result = new HashSet<>();

        if (!this.isCorrectRowNumber(_rowNumber)) {
            Logger.error("Numer wiersza jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _rowNumber);
            return result;
        }

        _rowNumber--;
        for (int col = 0; col < this.getBoardSize(); col++) {
            int value = this.board[_rowNumber][col].getValue();
            if (value != EMPTY_FIELD) {
                result.add(Math.abs(value));
            }
        }
        return result;
    }

    public Set<Integer> getUsedValuesFromCol(int _colNumber) {
        Set<Integer> result = new HashSet<>();

        if (!this.isCorrectColNumber(_colNumber)) {
            Logger.error("Numer kolumny jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _colNumber);
            return result;
        }

        _colNumber--;
        for (int row = 0; row < this.getBoardSize(); row++) {
            int value = this.board[row][_colNumber].getValue();
            if (value != EMPTY_FIELD) {
                result.add(Math.abs(value));
            }
        }
        return result;
    }

    public Set<Integer> getUsedValuesFromBlock(int _rowNumber, int _colNumber) {
        Set<Integer> result = new HashSet<>();

        if (!this.isCorrectRowNumber(_rowNumber)) {
            Logger.error("Numer wiersza jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _rowNumber);
            return result;
        }
        if (!this.isCorrectColNumber(_colNumber)) {
            Logger.error("Numer kolumny jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    this.getBoardSize(), _colNumber);
            return result;
        }

        int size = this.getSudokuSize();
        int rowStart = --_rowNumber / size * size;
        int rowStop = rowStart + size;
        int colStart = --_colNumber / size * size;
        int colStop = colStart + size;
        for (int row = rowStart; row < rowStop; row++) {
            for (int col = colStart; col < colStop; col++) {
                int value = this.board[row][col].getValue();
                if (value != EMPTY_FIELD) {
                    result.add(Math.abs(value));
                }
            }
        }
        return result;
    }

    public int[] getBoardData() {
        int[] result = new int[this.getBoardSize() * this.getBoardSize()];
        int index = 0;
        for (int row = 0; row < this.getBoardSize(); row++) {
            for (int col = 0; col < this.getBoardSize(); col++) {
                int value = this.board[row][col].getValue();
                if (this.board[row][col].isConstField()) {
                    value *= -1;
                }
                result[index++] = value;
            }
        }
        return result;
    }

    public void xxx_showBoard() {
        int sudokuSize = this.getSudokuSize();
        int boardSize = this.getBoardSize();
        String boardLine;
        int valueFormat;
        if (sudokuSize < 4) {
            boardLine = "---";
            valueFormat = 1;
        } else {
            boardLine = "----";
            valueFormat = 2;
        }

        System.out.println("\nRozmiar planszy: " + sudokuSize + "x" + sudokuSize);

        for (int row = 0; row < boardSize; row++) {
            if (row % sudokuSize == 00) {
                for (int col = 0; col < boardSize; col++) {
                    if (col % sudokuSize == 0) {
                        System.out.print("+");
                    }
                    System.out.print(boardLine);
                }
                System.out.println("+");
            }
            for (int col = 0; col < boardSize; col++) {
                if (col % sudokuSize == 0) {
                    System.out.print("|");
                }
                int value = this.board[row][col].getValue();
                if (this.board[row][col].isConstField()) {
                    System.out.printf("(%" + valueFormat + "d)", value);
                } else {
                    if (value != EMPTY_FIELD) {
                        System.out.printf(".%" + valueFormat + "d.", value);
                    } else {
                        System.out.printf(" %" + valueFormat + "s ", "");
                    }
                }
            }
            System.out.println("|");
        }
        for (int col = 0; col < boardSize; col++) {
            if (col % sudokuSize == 0) {
                System.out.print("+");
            }
            System.out.print(boardLine);
        }
        System.out.println("+");
    }

}

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

public class BoardTest {

    @Test
    public void testBoard2x2() {
        int[] testBoard2x2 = {
                0, -2, -4, 0,
                -1, 0, 0, -3,
                -4, 0, 0, -2,
                0, -1, -3, 0
        };
        Board board = new Board(testBoard2x2);
        // utworzenie planszy
        assertEquals(2, board.getSudokuSize());
        assertEquals(4, board.getBoardSize());
        assertEquals(4, board.getMaxValue());
        // załadowanie wartości - narożniki
        assertEquals(0, board.getValue(1, 1));
        assertEquals(0, board.getValue(1, 4));
        assertEquals(0, board.getValue(4, 1));
        assertEquals(0, board.getValue(4, 4));
        // załadowanie wartości - losowe pola
        assertEquals(4, board.getValue(3, 1));
        assertEquals(3, board.getValue(4, 3));

        Set<Integer> values = new TreeSet<>();
        // załadowanie wartości - wiersze
        values.clear();
        values.addAll(board.getUsedValuesFromRow(1));
        assertEquals("[2, 4]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromRow(4));
        assertEquals("[1, 3]", values.toString());
        // załadowanie wartości - kolumny
        values.clear();
        values.addAll(board.getUsedValuesFromCol(1));
        assertEquals("[1, 4]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromCol(4));
        assertEquals("[2, 3]", values.toString());
        // załadowanie wartości - bloki
        values.clear();
        values.addAll(board.getUsedValuesFromBlock(1, 1));
        assertEquals("[1, 2]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromBlock(3, 4));
        assertEquals("[2, 3]", values.toString());
    }

    @Test
    public void testBoard3x3() {
        int[] testBoard3x3 = {
                -9, 0, -6, 0, 0, -8, 0, 0, 0,
                0, 0, 0, 0, 0, -1, 0, -9, 0,
                -2, 0, 0, -9, -6, 0, -7, 0, 0,
                0, 0, 0, -1, 0, 0, 0, 0, 0,
                0, 0, -4, -2, -9, 0, 0, -5, 0,
                0, -5, 0, 0, 0, 0, -8, 0, 0,
                0, 0, 0, 0, 0, -6, 0, 0, 0,
                -7, 0, 0, 0, 0, 0, 0, 0, -3,
                0, 0, -2, -5, -4, 0, 0, -8, 0
        };
        Board board = new Board(testBoard3x3);
        // utworzenie planszy
        assertEquals(3, board.getSudokuSize());
        assertEquals(9, board.getBoardSize());
        assertEquals(9, board.getMaxValue());
        // załadowanie wartości - narożniki
        assertEquals(9, board.getValue(1, 1));
        assertEquals(0, board.getValue(1, 9));
        assertEquals(0, board.getValue(9, 1));
        assertEquals(0, board.getValue(9, 9));
        // załadowanie wartości - losowe pola
        assertEquals(7, board.getValue(3, 7));
        assertEquals(5, board.getValue(6, 2));

        Set<Integer> values = new TreeSet<>();
        // załadowanie wartości - wiersze
        values.clear();
        values.addAll(board.getUsedValuesFromRow(1));
        assertEquals("[6, 8, 9]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromRow(5));
        assertEquals("[2, 4, 5, 9]", values.toString());
        // załadowanie wartości - kolumny
        values.clear();
        values.addAll(board.getUsedValuesFromCol(4));
        assertEquals("[1, 2, 5, 9]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromCol(9));
        assertEquals("[3]", values.toString());
        // załadowanie wartości - bloki
        values.clear();
        values.addAll(board.getUsedValuesFromBlock(2, 5));
        assertEquals("[1, 6, 8, 9]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromBlock(9, 1));
        assertEquals("[2, 7]", values.toString());
    }

    @Test
    public void testBoard4x4() {
        int[] testBoard4x4 = {
                0, 0, -8, 0, 0, 0, -2, -16, 0, -14, 0, -11, -7, -10, 0, -4,
                0, -15, 0, 0, 0, 0, -8, 0, -13, 0, 0, -9, 0, 0, -16, -14,
                -14, 0, 0, -16, 0, 0, -11, -12, -8, -6, -2, -10, -13, -5, 0, -1,
                0, 0, -11, 0, 0, 0, 0, 0, -16, -7, -15, 0, 0, 0, 0, 0,
                -6, 0, -15, 0, 0, 0, -14, -7, 0, 0, -4, 0, 0, 0, -10, -13,
                0, -2, 0, 0, -1, 0, -15, -13, -3, 0, 0, -16, -5, -11, 0, 0,
                -5, 0, -3, 0, 0, 0, 0, 0, -15, -8, -6, 0, 0, 0, -14, 0,
                0, 0, -16, -7, -3, 0, -5, 0, 0, 0, -10, -2, -4, 0, 0, 0,
                0, -6, 0, -3, -14, -9, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0,
                -10, -8, -4, 0, 0, -1, 0, -2, 0, 0, 0, -12, -15, 0, 0, -16,
                -9, -1, -5, 0, 0, 0, 0, -10, 0, 0, 0, -7, 0, -3, -13, -12,
                -16, 0, -12, 0, 0, 0, -6, -11, 0, -1, 0, -13, -9, -2, -8, -10,
                0, -3, 0, 0, 0, -2, -1, -4, 0, -15, 0, 0, 0, 0, -11, -5,
                -11, 0, -13, 0, -6, 0, 0, -5, 0, -4, 0, -3, 0, -8, 0, 0,
                -15, -4, 0, 0, 0, 0, -3, 0, -2, 0, 0, 0, 0, 0, 0, 0,
                0, -14, 0, 0, 0, 0, -10, -15, -7, -5, -9, 0, -12, -13, 0, -3
        };
        Board board = new Board(testBoard4x4);
        // utworzenie planszy
        assertEquals(4, board.getSudokuSize());
        assertEquals(16, board.getBoardSize());
        assertEquals(16, board.getMaxValue());
        // załadowanie wartości - narożniki
        assertEquals(0, board.getValue(1, 1));
        assertEquals(4, board.getValue(1, 16));
        assertEquals(0, board.getValue(16, 1));
        assertEquals(3, board.getValue(16, 16));
        // załadowanie wartości - losowe pola
        assertEquals(10, board.getValue(11, 8));
        assertEquals(0, board.getValue(15, 14));

        Set<Integer> values = new TreeSet<>();
        // załadowanie wartości - wiersze
        values.clear();
        values.addAll(board.getUsedValuesFromRow(2));
        assertEquals("[8, 9, 13, 14, 15, 16]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromRow(12));
        assertEquals("[1, 2, 6, 8, 9, 10, 11, 12, 13, 16]", values.toString());
        // załadowanie wartości - kolumny
        values.clear();
        values.addAll(board.getUsedValuesFromCol(4));
        assertEquals("[3, 7, 16]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromCol(16));
        assertEquals("[1, 3, 4, 5, 10, 12, 13, 14, 16]", values.toString());
        // załadowanie wartości - bloki
        values.clear();
        values.addAll(board.getUsedValuesFromBlock(3, 11));
        assertEquals("[2, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromBlock(1, 1));
        assertEquals("[8, 11, 14, 15, 16]", values.toString());
    }

    @Test
    public void testBoard5x5() {
        int[] testBoard5x5 = {
                0, -2, 0, 0, 0, -3, -14, 0, -8, 0, 0, 0, 0, 0, 0, 0, 0, -13, -4, -24, 0, -7, -1, 0, 0,
                0, -10, -17, 0, 0, 0, -6, -18, 0, 0, -22, -16, 0, -12, 0, 0, 0, 0, -1, 0, 0, 0, -13, -19, 0,
                0, -15, -24, -13, -7, 0, 0, 0, -4, 0, -10, 0, 0, -3, -14, 0, -18, 0, 0, 0, 0, -22, -2, 6, 0,
                0, 0, -1, -21, 0, 0, -15, 0, -22, 0, 0, -19, -13, 0, 0, 0, -8, 0, 0, 0, 0, -16, -18, -20, 0,
                0, -5, 0, 0, -20, -7, -25, -19, 0, 0, 0, -21, -17, -18, -2, -10, -12, -22, -9, -15, -11, 0, 0, 0, 0,
                -11, 0, 0, 0, -22, -8, 0, -24, -7, -1, -5, 0, 0, 0, -13, -16, -17, -25, -23, -2, -4, 0, -6, 0, -19,
                -16, -9, -12, 0, -17, 0, -19, -22, 0, 0, 0, 0, -18, -21, 0, 0, -20, -6, -13, 0, -7, 0, 0, -23, -11,
                0, 0, -6, 0, -21, -9, -16, 0, -3, 0, 0, -22, -20, -19, 0, 0, 0, 0, -15, -8, -25, 0, 0, 0, 0,
                0, 0, -23, -5, 0, -2, 0, 0, -11, -17, -8, 0, 0, 0, -16, -12, -9, 0, 0, -21, 0, -3, -10, 0, 0,
                0, 0, 0, 0, 0, -6, 0, 0, -12, 0, -9, -1, -25, 0, -3, 0, -11, 0, 0, -7, 0, 0, -21, 0, 0,
                0, 0, -9, 0, 0, -23, 0, -5, -17, -4, -16, 0, -11, 0, -22, -18, -2, 0, -21, -13, 0, 0, -7, 0, 0,
                -4, -6, 0, 0, -5, 0, 0, -2, 0, 0, 0, -18, -21, -24, 0, 0, -19, -3, 0, -12, -23, 0, 0, -17, 0,
                0, 0, 0, -12, -11, 0, -7, -3, 0, -24, -17, -20, -15, -13, -19, -1, 0, -5, -8, 0, -6, -9, 0, 0, 0,
                0, -22, 0, 0, -14, -19, 0, -6, -16, 0, 0, -8, -9, -7, 0, 0, 0, -24, 0, 0, -3, 0, 0, -1, -18,
                0, 0, -21, 0, 0, -25, -13, 0, -20, -8, -12, 0, -14, 0, -10, -9, -16, -15, 0, -6, 0, 0, -4, 0, 0,
                0, 0, -25, 0, 0, -24, 0, 0, -18, 0, -4, 0, -3, -10, -5, 0, -1, 0, 0, -14, 0, 0, 0, 0, 0,
                0, 0, -5, -3, 0, -17, 0, 0, -23, -7, -13, 0, 0, 0, -18, -19, -21, 0, 0, -22, 0, -11, -12, 0, 0,
                0, 0, 0, 0, -18, -10, -8, 0, 0, 0, 0, -25, -23, -2, 0, 0, -5, 0, -16, -11, -9, 0, -3, 0, 0,
                -17, -20, 0, 0, -2, 0, -22, -16, -6, 0, 0, -7, -12, 0, 0, 0, 0, -9, -3, 0, -18, 0, -23, -24, -25,
                -6, 0, -4, 0, -16, -1, -11, -12, -25, -3, -19, 0, 0, 0, -21, -17, -23, -8, 0, -18, -2, 0, 0, 0, -14,
                0, 0, 0, 0, -4, -14, -24, -11, -19, -23, -21, -17, -16, -8, 0, 0, 0, -1, -2, -9, -13, 0, 0, -5, 0,
                0, -1, -14, -23, 0, 0, 0, 0, -9, 0, 0, 0, -19, -5, 0, 0, -24, 0, -12, 0, 0, -8, -17, 0, 0,
                0, -16, -11, -8, 0, 0, 0, 0, -1, 0, -6, -4, 0, 0, -23, 0, -15, 0, 0, 0, -14, -12, -9, -10, 0,
                0, -21, -3, 0, 0, 0, -17, 0, 0, 0, 0, -15, 0, -25, -20, 0, 0, -4, -10, 0, 0, 0, -16, -11, 0,
                0, 0, -20, -2, 0, -16, -5, -8, 0, 0, 0, 0, 0, 0, 0, 0, -6, 0, -19, -25, 0, 0, 0, -3, 0
        };
        Board board = new Board(testBoard5x5);
        // utworzenie planszy
        assertEquals(5, board.getSudokuSize());
        assertEquals(25, board.getBoardSize());
        assertEquals(25, board.getMaxValue());
        // załadowanie wartości - narożniki
        assertEquals(0, board.getValue(1, 1));
        assertEquals(0, board.getValue(1, 25));
        assertEquals(0, board.getValue(25, 1));
        assertEquals(0, board.getValue(25, 25));
        // załadowanie wartości - losowe pola
        assertEquals(6, board.getValue(10, 6));
        assertEquals(17, board.getValue(20, 16));

        Set<Integer> values = new TreeSet<>();
        // załadowanie wartości - wiersze
        values.clear();
        values.addAll(board.getUsedValuesFromRow(10));
        assertEquals("[1, 3, 6, 7, 9, 11, 12, 21, 25]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromRow(20));
        assertEquals("[1, 2, 3, 4, 6, 8, 11, 12, 14, 16, 17, 18, 19, 21, 23, 25]", values.toString());
        // załadowanie wartości - kolumny
        values.clear();
        values.addAll(board.getUsedValuesFromCol(4));
        assertEquals("[2, 3, 5, 8, 12, 13, 21, 23]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromCol(23));
        assertEquals("[1, 2, 3, 4, 6, 7, 9, 10, 12, 13, 16, 17, 18, 21, 23]", values.toString());
        // załadowanie wartości - bloki
        values.clear();
        values.addAll(board.getUsedValuesFromBlock(3, 17));
        assertEquals("[1, 4, 8, 9, 10, 12, 13, 15, 18, 22, 24]", values.toString());
        values.clear();
        values.addAll(board.getUsedValuesFromBlock(12, 12));
        assertEquals("[7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24]", values.toString());
    }

    @Test
    public void test_SetValue() {
        int[] testBoard2x2 = {
                0, -2, -4, 0,
                -1, 0, 0, -3,
                -4, 0, 0, -2,
                0, -1, -3, 0
        };
        Board board = new Board(testBoard2x2);

        // dodawanie wartości w pustym polu
        board.setValue(1, 1, 3);
        assertEquals(3, board.getValue(1, 1));

        board.setValue(2, 2, 4);
        assertEquals(4, board.getValue(2, 2));

        // dodawanie wartości w polu stałym
        board.setValue(4, 3, 2);
        assertEquals(3, board.getValue(4, 3));

        // dodawanie wartości w polu zmiennym mającym już wartość
        board.setValue(1, 1, 4);
        // assertEquals(3, board.getValue(1, 1)); // przy włączonej kontroli możliwości
        assertEquals(4, board.getValue(1, 1)); // przy wyłączonej kontroli możliwości
    }

    @Test
    public void test_removeValue() {
        int[] testBoard2x2 = {
                0, -2, -4, 0,
                -1, 0, 0, -3,
                -4, 0, 0, -2,
                0, -1, -3, 0
        };
        Board board = new Board(testBoard2x2);
        board.setValue(1, 1, 3);
        board.setValue(2, 2, 4);

        // usuwanie wartości w polu zmiennym mającym wartość
        board.removeValue(1, 1);
        assertEquals(0, board.getValue(1, 1));
        board.removeValue(2, 2);
        assertEquals(0, board.getValue(2, 2));

        // usuwanie wartości w polu stałym
        board.removeValue(1, 2);
        assertEquals(2, board.getValue(1, 2));

        // usuwanie wartości w polu pustym
        board.removeValue(4, 4);
        assertEquals(0, board.getValue(4, 4));
    }
}

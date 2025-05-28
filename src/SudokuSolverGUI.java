import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class SudokuSolverGUI {
    private static String BOARD_2X_CORNER_TOP_LEFT = String.valueOf((char) 0x2554);
    private static String BOARD_2X_CORNER_TOP_RIGHT = String.valueOf((char) 0x2557);
    private static String BOARD_2X_CORNER_BOTTOM_LEFT = String.valueOf((char) 0x255A);
    private static String BOARD_2X_CORNER_BOTTOM_RIGHT = String.valueOf((char) 0x255D);
    private static String BOARD_2X_HORI = String.valueOf((char) 0x2550);
    private static String BOARD_2X_HORI_2X_UP = String.valueOf((char) 0x2569);
    private static String BOARD_2X_HORI_2X_DOWN = String.valueOf((char) 0x2566);
    private static String BOARD_2X_HORI_1X_UP = String.valueOf((char) 0x2567);
    private static String BOARD_2X_HORI_1X_UP_DOWN = String.valueOf((char) 0x256A);
    private static String BOARD_2X_HORI_1X_DOWN = String.valueOf((char) 0x2564);
    private static String BOARD_2X_VERT = String.valueOf((char) 0x2551);
    private static String BOARD_2X_VERT_2X_LEFT = String.valueOf((char) 0x2563);
    private static String BOARD_2X_VERT_2X_RIGHT = String.valueOf((char) 0x2560);
    private static String BOARD_2X_VERT_1X_LEFT = String.valueOf((char) 0x2562);
    private static String BOARD_2X_VERT_1X_RIGHT = String.valueOf((char) 0x255F);
    private static String BOARD_2X_CROSS = String.valueOf((char) 0x256C);

    private static int[] testBoard2x2 = {
            0, -2, -4, 0,
            -1, 0, 0, -3,
            -4, 0, 0, -2,
            0, -1, -3, 0
    };
    private static int[] testBoard3x3 = {
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
    private static int[] testBoard4x4 = {
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
    private static int[] testBoard5x5 = {
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

    private static Board screenBoard = new Board(testBoard5x5);
    private static Terminal terminal = null;
    private static TextGraphics constField;
    private static TextGraphics fluidField;
    private static TextGraphics boardField;

    public static void main(String[] args) throws InterruptedException {
        try {
            // defaultTerminalFactory = new DefaultTerminalFactory();
            terminal = new DefaultTerminalFactory(System.out, System.in, Charset.defaultCharset())
                    .setInitialTerminalSize(new TerminalSize(104, 100))
                    .setTerminalEmulatorFrameAutoCloseTrigger(TerminalEmulatorAutoCloseTrigger.CloseOnEscape)
                    .setTerminalEmulatorTitle("Sudoku Solver by T.Trawka")
                    .setPreferTerminalEmulator(true)
                    .createTerminal();
            // terminal.enterPrivateMode();
            terminal.clearScreen();
            terminal.setCursorVisible(false);

            // Runtime.getRuntime().addShutdownHook(
            // new Thread(() -> {
            // try {
            // if (terminal != null) {
            // terminal.close();
            // }
            // System.exit(0);
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            // }));

            constField = terminal.newTextGraphics();
            constField.setForegroundColor(TextColor.ANSI.BLACK);
            constField.setBackgroundColor(TextColor.ANSI.WHITE);
            fluidField = terminal.newTextGraphics();
            fluidField.setForegroundColor(TextColor.ANSI.WHITE);
            fluidField.setBackgroundColor(TextColor.ANSI.BLACK);
            boardField = terminal.newTextGraphics();
            boardField.setForegroundColor(TextColor.ANSI.YELLOW);
            boardField.setBackgroundColor(TextColor.ANSI.BLACK);

            drawSudokuBoard(5, 5);

            // textGraphics.putString(5, 3, "Terminal Bold: ", SGR.BOLD);
            // textGraphics.putString(5, 4, "Terminal Bordered: ", SGR.BORDERED);
            // textGraphics.putString(5, 5, "Terminal italic: ", SGR.ITALIC);
            // textGraphics.putString(5, 6, "Terminal blink: ", SGR.BLINK);
            // textGraphics.putString(5, 7, "Terminal crossed out: ", SGR.CROSSED_OUT);
            // textGraphics.putString(5, 8, "Terminal circled: ", SGR.CIRCLED);

            terminal.flush();

            KeyStroke keyStroke = terminal.readInput();
            while (keyStroke.getKeyType() != KeyType.Escape) {
                // textGraphics.drawLine(5, 4, terminal.getTerminalSize().getColumns() - 1, 4, '
                // ');
                // textGraphics.putString(5, 4, "Last Keystroke: ", SGR.BOLD);
                // textGraphics.putString(5 + "Last Keystroke: ".length(), 4,
                // keyStroke.toString());
                terminal.flush();
                keyStroke = terminal.readInput();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (terminal != null) {
                try {
                    terminal.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void drawSudokuBoard(int _startRow, int _startCol) {
        int sudSize = screenBoard.getSudokuSize();
        int boardSize = screenBoard.getBoardSize();
        int blockWidth = sudSize * 2 + sudSize - 1;
        int colMax = sudSize * blockWidth + sudSize + 1;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int actCol = _startCol + 1 + col * 3;
                int actRow = _startRow + 1 + row + (row / sudSize);
                if (row == 0) {
                    if (col == 0) {
                        boardField.putString(actCol - 1, actRow - 1, BOARD_2X_CORNER_TOP_LEFT);
                        boardField.putString(actCol + 2, actRow - 1, BOARD_2X_HORI_1X_DOWN);
                    } else if (col == boardSize - 1) {
                        boardField.putString(actCol + 2, actRow - 1, BOARD_2X_CORNER_TOP_RIGHT);
                    } else if (col % sudSize == sudSize - 1) {
                        boardField.putString(actCol + 2, actRow - 1, BOARD_2X_HORI_2X_DOWN);
                    } else {
                        boardField.putString(actCol + 2, actRow - 1, BOARD_2X_HORI_1X_DOWN);
                    }
                    boardField.putString(actCol, actRow - 1, BOARD_2X_HORI + BOARD_2X_HORI);
                } else if (row == boardSize - 1) {
                    if (col == 0) {
                        boardField.putString(actCol - 1, actRow + 1, BOARD_2X_CORNER_BOTTOM_LEFT);
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_HORI_1X_UP);
                    } else if (col == boardSize - 1) {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_CORNER_BOTTOM_RIGHT);
                    } else if (col % sudSize == sudSize - 1) {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_HORI_2X_UP);
                    } else {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_HORI_1X_UP);
                    }
                    boardField.putString(actCol, actRow + 1, BOARD_2X_HORI + BOARD_2X_HORI);
                } else if (row % sudSize == sudSize - 1) {
                    if (col == 0) {
                        boardField.putString(actCol - 1, actRow + 1, BOARD_2X_VERT_2X_RIGHT);
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_HORI_1X_UP_DOWN);
                    } else if (col == boardSize - 1) {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_VERT_2X_LEFT);
                    } else if (col % sudSize == sudSize - 1) {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_CROSS);
                    } else {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_HORI_1X_UP_DOWN);
                    }
                    boardField.putString(actCol, actRow + 1, BOARD_2X_HORI + BOARD_2X_HORI);
                }

                if (col == 0) { // lewa ramka w wierszu
                    boardField.putString(actCol - 1, actRow, BOARD_2X_VERT);
                } else if (col == boardSize - 1) {
                    boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                } else if (col % sudSize == sudSize - 1) {
                    boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                }

                int fieldValue = Math.abs(screenBoard.getValue(row + 1, col + 1));

                if (screenBoard.isConstField(row + 1, col + 1)) {
                    constField.putString(actCol, actRow, String.format("%2d", fieldValue), SGR.BOLD);
                } else {
                    if (fieldValue == BoardBase.EMPTY_FIELD) {
                        fluidField.putString(actCol, actRow, "  ");
                    } else {
                        fluidField.putString(actCol, actRow, String.format("%2d", fieldValue));
                    }
                }
            }
        }
    }
}
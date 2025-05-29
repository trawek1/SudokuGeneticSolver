import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

import org.tinylog.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

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
    // private static String BOARD_2X_VERT_1X_LEFT = String.valueOf((char) 0x2562);
    // private static String BOARD_2X_VERT_1X_RIGHT = String.valueOf((char) 0x255F);
    private static String BOARD_2X_CROSS = String.valueOf((char) 0x256C);
    private static String BOARD_1X_VERT = String.valueOf((char) 0x2502);
    private static String TITLE_BLOCK = String.valueOf((char) 0x2588);

    private static int TERMINAL_WIDTH = 80;
    private static int TERMINAL_HEIGHT = 57;

    // private static int[] testBoard2x2 = {
    // 0, -2, -4, 0,
    // -1, 0, 0, -3,
    // -4, 0, 0, -2,
    // 0, -1, -3, 0
    // };
    // private static int[] testBoard3x3 = {
    // -9, 0, -6, 0, 0, -8, 0, 0, 0,
    // 0, 0, 0, 0, 0, -1, 0, -9, 0,
    // -2, 0, 0, -9, -6, 0, -7, 0, 0,
    // 0, 0, 0, -1, 0, 0, 0, 0, 0,
    // 0, 0, -4, -2, -9, 0, 0, -5, 0,
    // 0, -5, 0, 0, 0, 0, -8, 0, 0,
    // 0, 0, 0, 0, 0, -6, 0, 0, 0,
    // -7, 0, 0, 0, 0, 0, 0, 0, -3,
    // 0, 0, -2, -5, -4, 0, 0, -8, 0
    // };
    // private static int[] testBoard4x4 = {
    // 0, 0, -8, 0, 0, 0, -2, -16, 0, -14, 0, -11, -7, -10, 0, -4,
    // 0, -15, 0, 0, 0, 0, -8, 0, -13, 0, 0, -9, 0, 0, -16, -14,
    // -14, 0, 0, -16, 0, 0, -11, -12, -8, -6, -2, -10, -13, -5, 0, -1,
    // 0, 0, -11, 0, 0, 0, 0, 0, -16, -7, -15, 0, 0, 0, 0, 0,
    // -6, 0, -15, 0, 0, 0, -14, -7, 0, 0, -4, 0, 0, 0, -10, -13,
    // 0, -2, 0, 0, -1, 0, -15, -13, -3, 0, 0, -16, -5, -11, 0, 0,
    // -5, 0, -3, 0, 0, 0, 0, 0, -15, -8, -6, 0, 0, 0, -14, 0,
    // 0, 0, -16, -7, -3, 0, -5, 0, 0, 0, -10, -2, -4, 0, 0, 0,
    // 0, -6, 0, -3, -14, -9, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0,
    // -10, -8, -4, 0, 0, -1, 0, -2, 0, 0, 0, -12, -15, 0, 0, -16,
    // -9, -1, -5, 0, 0, 0, 0, -10, 0, 0, 0, -7, 0, -3, -13, -12,
    // -16, 0, -12, 0, 0, 0, -6, -11, 0, -1, 0, -13, -9, -2, -8, -10,
    // 0, -3, 0, 0, 0, -2, -1, -4, 0, -15, 0, 0, 0, 0, -11, -5,
    // -11, 0, -13, 0, -6, 0, 0, -5, 0, -4, 0, -3, 0, -8, 0, 0,
    // -15, -4, 0, 0, 0, 0, -3, 0, -2, 0, 0, 0, 0, 0, 0, 0,
    // 0, -14, 0, 0, 0, 0, -10, -15, -7, -5, -9, 0, -12, -13, 0, -3
    // };
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
                    .setInitialTerminalSize(new TerminalSize(TERMINAL_WIDTH, TERMINAL_HEIGHT))
                    .setTerminalEmulatorFrameAutoCloseTrigger(TerminalEmulatorAutoCloseTrigger.CloseOnEscape)
                    .setTerminalEmulatorTitle("Sudoku Solver")
                    .setPreferTerminalEmulator(true)
                    .createTerminal();
            // terminal.enterPrivateMode();
            terminal.clearScreen();
            terminal.setCursorVisible(false);

            prepareScreenElements();
            drawAll();
            terminal.flush();

            KeyStroke keyStroke = terminal.readInput();
            while (keyStroke.getKeyType() != KeyType.Escape) {
                if (keyStroke.getKeyType() == KeyType.F1) {
                    SolverGeneticParentMaker.changeParentGeneratingMethod();
                } else if (keyStroke.getKeyType() == KeyType.F2) {
                    SolverGeneticPopulation.changeBestParentsPercentByStep();
                } else if (keyStroke.getKeyType() == KeyType.F3) {
                    SolverGeneticPopulation.changeParentSelectionMethod();
                } else if (keyStroke.getKeyType() == KeyType.F4) {
                    SolverGeneticCrossover.changeCrossoverMethod();
                } else if (keyStroke.getKeyType() == KeyType.F5) {
                    SolverGeneticCrossover.changeMutationProbabilityByStep();
                } else if (keyStroke.getKeyType() == KeyType.F6) {
                    SolverGeneticIndividual.changeFitnessCalculatingMethod();
                } else if (keyStroke.getKeyType() == KeyType.F7) {
                    SolverGeneticPopulation.changePopulationSizeByStep();
                }

                drawAll();
                terminal.flush();

                keyStroke = terminal.readInput();
            }

        } catch (IOException e) {
            Logger.error("Wystąpił błąd podczas pracy programu! {}", e.getMessage());
        } finally {
            if (terminal != null) {
                try {
                    terminal.close();
                } catch (IOException e) {
                    Logger.error("Wystąpił błąd podczas zamykania programu! {}", e.getMessage());
                }
            }
        }
    }

    public static void prepareScreenElements() {
        try {
            constField = terminal.newTextGraphics();
            constField.setForegroundColor(TextColor.ANSI.GREEN);
            constField.setBackgroundColor(TextColor.ANSI.BLACK);
            fluidField = terminal.newTextGraphics();
            fluidField.setForegroundColor(TextColor.ANSI.WHITE);
            fluidField.setBackgroundColor(TextColor.ANSI.BLACK);
            boardField = terminal.newTextGraphics();
            boardField.setForegroundColor(TextColor.ANSI.YELLOW);
            boardField.setBackgroundColor(TextColor.ANSI.BLACK);
        } catch (Exception e) {
            Logger.error("Wystąpił błąd podczas pracy programu! {}", e.getMessage());
            System.exit(0);
        }
    }

    public static void drawAll() {
        int titleWidth = 51;
        int boardWidth = 0;
        int boardHeight = 0;
        switch (screenBoard.getSudokuSize()) {
            case 2 -> {
                boardWidth = 13;
                boardHeight = 7;
            }
            case 3 -> {
                boardWidth = 28;
                boardHeight = 13;
            }
            case 4 -> {
                boardWidth = 49;
                boardHeight = 21;
            }
            case 5 -> {
                boardWidth = 76;
                boardHeight = 31;
            }
            default -> {
                Logger.warn("Wartość jest spoza zakresu! Oczekiwano {}-{}, otrzymano {}. Zakończono program.",
                        BoardBase.SUDOKU_SIZE_MIN, BoardBase.SUDOKU_SIZE_MAX, screenBoard.getSudokuSize());
                System.exit(0);
            }
        }
        drawTitle(1, (TERMINAL_WIDTH - titleWidth) / 2);
        drawSudokuBoard(8, (TERMINAL_WIDTH - boardWidth) / 2);
        drawMenu(8 + boardHeight + 1, 2);

        try {
            terminal.flush();
        } catch (Exception e) {
            Logger.error("Wystąpił błąd podczas pracy programu! {}", e.getMessage());
            System.exit(0);
        }
    }

    public static void drawTitle(int _startRow, int _startCol) {
        boardField.putString(_startCol, _startRow, TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + "  "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + "     "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + "   "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " ");
        boardField.putString(_startCol, _startRow + 1, TITLE_BLOCK + "   "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + "     "
                + TITLE_BLOCK + "   "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + "   "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + "   "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " ");
        boardField.putString(_startCol, _startRow + 2, TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + "  "
                + TITLE_BLOCK + " " + TITLE_BLOCK + "     "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + "   "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + "  "
                + TITLE_BLOCK + TITLE_BLOCK + "  ");
        boardField.putString(_startCol, _startRow + 3, "  " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + "     "
                + "  " + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + "   "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + "   "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " ");
        boardField.putString(_startCol, _startRow + 4, TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + "     "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + " " + TITLE_BLOCK + "  "
                + TITLE_BLOCK + TITLE_BLOCK + TITLE_BLOCK + " "
                + TITLE_BLOCK + " " + TITLE_BLOCK + " ");
        boardField.putString(_startCol + 17, _startRow + 5, "Tomasz     Trawka");
    }

    public static void drawSudokuBoard(int _startRow, int _startCol) {
        int sudSize = screenBoard.getSudokuSize();
        int boardSize = screenBoard.getBoardSize();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int actCol = _startCol + 1 + col * 3;
                int actRow = _startRow + 1 + row + (row / sudSize);

                if (row == 0) {
                    if (col == 0) {
                        boardField.putString(actCol - 1, actRow - 1, BOARD_2X_CORNER_TOP_LEFT);
                        boardField.putString(actCol + 2, actRow - 1, BOARD_2X_HORI_1X_DOWN);
                        boardField.putString(actCol - 1, actRow, BOARD_2X_VERT);
                        boardField.putString(actCol + 2, actRow, BOARD_1X_VERT);
                    } else if (col == boardSize - 1) {
                        boardField.putString(actCol + 2, actRow - 1, BOARD_2X_CORNER_TOP_RIGHT);
                        boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                    } else if (col % sudSize == sudSize - 1) {
                        boardField.putString(actCol + 2, actRow - 1, BOARD_2X_HORI_2X_DOWN);
                        boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                    } else {
                        boardField.putString(actCol + 2, actRow - 1, BOARD_2X_HORI_1X_DOWN);
                        boardField.putString(actCol + 2, actRow, BOARD_1X_VERT);
                    }
                    boardField.putString(actCol, actRow - 1, BOARD_2X_HORI + BOARD_2X_HORI);
                } else if (row == boardSize - 1) {
                    if (col == 0) {
                        boardField.putString(actCol - 1, actRow + 1, BOARD_2X_CORNER_BOTTOM_LEFT);
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_HORI_1X_UP);
                        boardField.putString(actCol - 1, actRow, BOARD_2X_VERT);
                        boardField.putString(actCol + 2, actRow, BOARD_1X_VERT);
                    } else if (col == boardSize - 1) {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_CORNER_BOTTOM_RIGHT);
                        boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                    } else if (col % sudSize == sudSize - 1) {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_HORI_2X_UP);
                        boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                    } else {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_HORI_1X_UP);
                        boardField.putString(actCol + 2, actRow, BOARD_1X_VERT);
                    }
                    boardField.putString(actCol, actRow + 1, BOARD_2X_HORI + BOARD_2X_HORI);
                } else if (row % sudSize == sudSize - 1) {
                    if (col == 0) {
                        boardField.putString(actCol - 1, actRow + 1, BOARD_2X_VERT_2X_RIGHT);
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_HORI_1X_UP_DOWN);
                        boardField.putString(actCol - 1, actRow, BOARD_2X_VERT);
                        boardField.putString(actCol + 2, actRow, BOARD_1X_VERT);
                    } else if (col == boardSize - 1) {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_VERT_2X_LEFT);
                        boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                    } else if (col % sudSize == sudSize - 1) {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_CROSS);
                        boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                    } else {
                        boardField.putString(actCol + 2, actRow + 1, BOARD_2X_HORI_1X_UP_DOWN);
                        boardField.putString(actCol + 2, actRow, BOARD_1X_VERT);
                    }
                    boardField.putString(actCol, actRow + 1, BOARD_2X_HORI + BOARD_2X_HORI);
                } else {
                    if (col == 0) {
                        boardField.putString(actCol - 1, actRow, BOARD_2X_VERT);
                        boardField.putString(actCol + 2, actRow, BOARD_1X_VERT);
                    } else if (col == boardSize - 1) {
                        boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                    } else if (col % sudSize == sudSize - 1) {
                        boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                    } else {
                        boardField.putString(actCol + 2, actRow, BOARD_1X_VERT);
                    }
                }

                // if (col == 0) {
                // boardField.putString(actCol - 1, actRow, BOARD_2X_VERT);
                // } else if (col == boardSize - 1) {
                // boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                // } else if (col % sudSize == sudSize - 1) {
                // boardField.putString(actCol + 2, actRow, BOARD_2X_VERT);
                // }

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

    public static void drawMenu(int _startRow, int _startCol) {
        final int colWidth = 44;
        int actRow = _startRow;
        int actCol = _startCol;
        int namesCol = _startCol + 4;
        String optionName;
        String optionValue;

        // TODO >>>>> dokończyć menu (R,M,F8,F12)
        boardField.putString(actCol, actRow, "=== MENU ===");
        actRow++;
        boardField.putString(actCol, actRow, "R ");
        fluidField.putString(namesCol, actRow, "Rozmiar planszy");
        actRow++;
        boardField.putString(actCol, actRow, "D ");
        fluidField.putString(namesCol, actRow, "Dane wejściowe");
        actRow++;
        boardField.putString(actCol, actRow, "M ");
        fluidField.putString(namesCol, actRow, "Metoda rozwiązywania");

        actRow++;
        optionName = "- generator rodziców: ";
        optionValue = String.format("%-" + (colWidth - optionName.length()) + "s",
                SolverGeneticParentMaker.getParentGeneratingMethodName());
        boardField.putString(actCol, actRow, "F1");
        fluidField.putString(namesCol, actRow, optionName);
        fluidField.putString(namesCol + optionName.length(), actRow, optionValue, SGR.BOLD);

        actRow++;
        optionName = "- pula rodziców (% populacji): ";
        optionValue = String.format("%-" + (colWidth - optionName.length()) + "d",
                SolverGeneticPopulation.getBestParentsPercent());
        boardField.putString(actCol, actRow, "F2");
        fluidField.putString(namesCol, actRow, optionName);
        fluidField.putString(namesCol + optionName.length(), actRow, optionValue, SGR.BOLD);

        actRow++;
        optionName = "- selekcja rodziców: ";
        optionValue = String.format("%-" + (colWidth - optionName.length()) + "s",
                SolverGeneticPopulation.getParentSelectionMethodName());
        boardField.putString(actCol, actRow, "F3");
        fluidField.putString(namesCol, actRow, optionName);
        fluidField.putString(namesCol + optionName.length(), actRow, optionValue, SGR.BOLD);

        actRow++;
        optionName = "- metoda krzyżowania: ";
        optionValue = String.format("%-" + (colWidth - optionName.length()) + "s",
                SolverGeneticCrossover.getCrossoverMethodName());
        boardField.putString(actCol, actRow, "F4");
        fluidField.putString(namesCol, actRow, optionName);
        fluidField.putString(namesCol + optionName.length(), actRow, optionValue, SGR.BOLD);

        actRow++;
        optionName = "- skala mutacji (%): ";
        optionValue = String.format("%-" + (colWidth - optionName.length()) + "d",
                SolverGeneticCrossover.getMutationProbability());
        boardField.putString(actCol, actRow, "F5");
        fluidField.putString(namesCol, actRow, optionName);
        fluidField.putString(namesCol + optionName.length(), actRow, optionValue, SGR.BOLD);

        actRow++;
        optionName = "- metoda dopasowania: ";
        optionValue = String.format("%-" + (colWidth - optionName.length()) + "s",
                SolverGeneticIndividual.getFitnessCalculatingMethodName());
        boardField.putString(actCol, actRow, "F6");
        fluidField.putString(namesCol, actRow, optionName);
        fluidField.putString(namesCol + optionName.length(), actRow, optionValue, SGR.BOLD);

        actRow++;
        optionName = "- wielkość populacji: ";
        optionValue = String.format("%-" + (colWidth - optionName.length()) + "d",
                SolverGeneticPopulation.getPopulationSize());
        boardField.putString(actCol, actRow, "F7");
        fluidField.putString(namesCol, actRow, optionName);
        fluidField.putString(namesCol + optionName.length(), actRow, optionValue, SGR.BOLD);

        actRow++;
        boardField.putString(actCol, actRow, "F8");
        fluidField.putString(namesCol, actRow, "- ilość generacji");

        actRow++;
        actRow++;
        boardField.putString(actCol, actRow, "F12");
        fluidField.putString(namesCol, actRow, "START / STOP");

        actRow++;
        actRow++;
        boardField.putString(actCol, actRow, "ESC");
        fluidField.putString(namesCol, actRow, "Koniec programu");
    }

}
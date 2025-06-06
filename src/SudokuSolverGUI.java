import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

import org.tinylog.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

public class SudokuSolverGUI extends SolverBase {
    private static final String BOARD_2X_CORNER_TOP_LEFT = String.valueOf((char) 0x2554);
    private static final String BOARD_2X_CORNER_TOP_RIGHT = String.valueOf((char) 0x2557);
    private static final String BOARD_2X_CORNER_BOTTOM_LEFT = String.valueOf((char) 0x255A);
    private static final String BOARD_2X_CORNER_BOTTOM_RIGHT = String.valueOf((char) 0x255D);
    private static final String BOARD_2X_HORI = String.valueOf((char) 0x2550);
    private static final String BOARD_2X_HORI_2X_UP = String.valueOf((char) 0x2569);
    private static final String BOARD_2X_HORI_2X_DOWN = String.valueOf((char) 0x2566);
    private static final String BOARD_2X_HORI_1X_UP = String.valueOf((char) 0x2567);
    private static final String BOARD_2X_HORI_1X_UP_DOWN = String.valueOf((char) 0x256A);
    private static final String BOARD_2X_HORI_1X_DOWN = String.valueOf((char) 0x2564);
    private static final String BOARD_2X_VERT = String.valueOf((char) 0x2551);
    private static final String BOARD_2X_VERT_2X_LEFT = String.valueOf((char) 0x2563);
    private static final String BOARD_2X_VERT_2X_RIGHT = String.valueOf((char) 0x2560);
    // private static String BOARD_2X_VERT_1X_LEFT = String.valueOf((char) 0x2562);
    // private static String BOARD_2X_VERT_1X_RIGHT = String.valueOf((char) 0x255F);
    private static final String BOARD_2X_CROSS = String.valueOf((char) 0x256C);
    private static final String BOARD_1X_VERT = String.valueOf((char) 0x2502);
    private static final String TITLE_BLOCK = String.valueOf((char) 0x2588);
    private static final int TERMINAL_WIDTH = 80;
    private static final int TERMINAL_HEIGHT = 57;

    private static final int GENERATIONS_NUMBER_MIN = 5_000;
    private static final int GENERATIONS_NUMBER_MAX = 500_000;
    private static final int GENERATIONS_NUMBER_STEP = 5_000;
    private static final int GENERATIONS_NUMBER_DEFAULT = 50_000;
    private static int generationsNumber;
    private static SolvingMethodsEnum solvingMethod;
    public static SudokuBoardsEnum sudokuTestBoard;
    public static Board boardForScreen;

    private static Terminal terminal = null;
    private static Screen screen = null;
    private static TextGraphics constField;
    private static TextGraphics fluidField;
    private static TextGraphics boardField;

    /**
     * ==============================================================================
     * =================================================================MET.STATYCZNE
     * ==============================================================================
     */

    static {
        sudokuTestBoard = SudokuBoardsEnum.X3N;
        solvingMethod = SolvingMethodsEnum.GENETIC_X01;
        generationsNumber = GENERATIONS_NUMBER_DEFAULT;
        boardForScreen = new Board(sudokuTestBoard.getBoardData());
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            terminal = new DefaultTerminalFactory(System.out, System.in, Charset.defaultCharset())
                    .setInitialTerminalSize(new TerminalSize(TERMINAL_WIDTH, TERMINAL_HEIGHT))
                    .setTerminalEmulatorFrameAutoCloseTrigger(TerminalEmulatorAutoCloseTrigger.CloseOnEscape)
                    .setTerminalEmulatorTitle("Sudoku Solver")
                    .setPreferTerminalEmulator(true)
                    .createTerminal();
            terminal.setCursorVisible(false);

            screen = new TerminalScreen(terminal);
            screen.startScreen();
            screen.setCursorPosition(null);
            screen.clear();

            prepareScreenElements();
            drawAll();

            KeyStroke keyStroke = screen.pollInput();
            boolean mustRefresh = true;
            while (true) {
                keyStroke = screen.pollInput();
                if (keyStroke == null) {
                    TerminalSize newSize = screen.doResizeIfNecessary();
                    if (newSize != null) {
                        mustRefresh = true;
                    }
                } else {
                    if (keyStroke.getKeyType() == KeyType.Escape) {
                        break;
                    } else if (keyStroke.getKeyType() == KeyType.Character
                            && (keyStroke.getCharacter() == 'R' || keyStroke.getCharacter() == 'r')) {
                        changeSudokuTestBoard();
                    } else if (keyStroke.getKeyType() == KeyType.Character
                            && (keyStroke.getCharacter() == 'M' || keyStroke.getCharacter() == 'm')) {
                        changeSolvingMethod();
                    } else if (keyStroke.getKeyType() == KeyType.F1) {
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
                    } else if (keyStroke.getKeyType() == KeyType.F8) {
                        changeGenerationsNumberByStep();
                    } else if (keyStroke.getKeyType() == KeyType.F12) {
                        startStopSolving();
                    }
                    mustRefresh = true;
                }

                if (SolvingInfo.isChanged()) {
                    mustRefresh = true;
                    SolvingInfo.setToNoChanged();
                }

                if (mustRefresh) {
                    drawAll();
                    mustRefresh = false;
                }
                Thread.yield();
            }

        } catch (IOException e) {
            Logger.error("Wystąpił błąd podczas pracy programu! {}", e.getMessage());
        } finally {
            if (screen != null) {
                try {
                    screen.close();
                } catch (IOException e) {
                    Logger.error("Wystąpił błąd podczas zamykania programu! {}", e.getMessage());
                }
            }
            if (terminal != null) {
                try {
                    terminal.exitPrivateMode();
                    terminal.close();
                } catch (IOException e) {
                    Logger.error("Wystąpił błąd podczas zamykania programu! {}", e.getMessage());
                }
            }
        }
    }

    public static String getSudokuTestBoardName() {
        return sudokuTestBoard.getDisplayName();
    }

    public static SudokuBoardsEnum changeSudokuTestBoard() {
        sudokuTestBoard = sudokuTestBoard.next();
        boardForScreen = new Board(sudokuTestBoard.getBoardData());
        return sudokuTestBoard;
    }

    public static String getSolvingMethodName() {
        return solvingMethod.getDisplayName();
    }

    public static SolvingMethodsEnum changeSolvingMethod() {
        return solvingMethod = solvingMethod.next();
    }

    public static int getGenerationsNumber() {
        return generationsNumber;
    }

    public static void changeGenerationsNumberByStep() {
        generationsNumber += GENERATIONS_NUMBER_STEP;
        if (generationsNumber > GENERATIONS_NUMBER_MAX) {
            generationsNumber = GENERATIONS_NUMBER_MIN;
        }
    }

    public static void startStopSolving() {
        if (SolvingInfo.getStatus() == SolvingStatusEnum.IN_PROGRESS) {
            SolvingInfo.changeStatusTo(SolvingStatusEnum.STOPPED_BY_USER);
        } else {
            SolvingInfo.changeStatusTo(SolvingStatusEnum.IN_PROGRESS);
            startSolving();
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
        switch (boardForScreen.getSudokuSize()) {
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
                        BoardBase.SUDOKU_SIZE_MIN, BoardBase.SUDOKU_SIZE_MAX,
                        boardForScreen.getSudokuSize());
                System.exit(0);
            }
        }

        try {
            screen.clear();
            screen.refresh();
            terminal.flush();
        } catch (Exception e) {
            Logger.error("Wystąpił błąd podczas pracy programu! {}", e.getMessage());
            System.exit(0);
        }

        drawTitle(1, (TERMINAL_WIDTH - titleWidth) / 2);
        drawSudokuBoard(8, (TERMINAL_WIDTH - boardWidth) / 2);
        drawMenu(8 + boardHeight + 1, 2);
        drawSolvingInfo(8 + boardHeight + 1, 52);

        try {
            screen.refresh();
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
        int sudSize = boardForScreen.getSudokuSize();
        int boardSize = boardForScreen.getBoardSize();

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

                int fieldValue = Math.abs(boardForScreen.getValue(row + 1, col + 1));

                if (boardForScreen.isConstField(row + 1, col + 1)) {
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

        boardField.putString(actCol, actRow, "=== MENU ===");

        actRow++;
        optionName = "Rozmiar planszy: ";
        optionValue = String.format("%-" + (colWidth - optionName.length()) + "s", getSudokuTestBoardName());
        boardField.putString(actCol, actRow, "R");
        fluidField.putString(namesCol, actRow, optionName);
        fluidField.putString(namesCol + optionName.length(), actRow, optionValue, SGR.BOLD);

        actRow++;
        optionName = "Metoda rozwiązywania: ";
        optionValue = String.format("%-" + (colWidth - optionName.length()) + "s", getSolvingMethodName());
        boardField.putString(actCol, actRow, "M");
        fluidField.putString(namesCol, actRow, optionName);
        fluidField.putString(namesCol + optionName.length(), actRow, optionValue, SGR.BOLD);

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
        optionName = "- ilość generacji: ";
        optionValue = String.format("%-" + (colWidth - optionName.length()) + "d", getGenerationsNumber());
        boardField.putString(actCol, actRow, "F8");
        fluidField.putString(namesCol, actRow, optionName);
        fluidField.putString(namesCol + optionName.length(), actRow, optionValue, SGR.BOLD);

        actRow++;
        if (SolvingInfo.getStatus() == SolvingStatusEnum.IN_PROGRESS) {
            optionName = "PRZERWIJ rozwiązywanie sudoku";
        } else {
            optionName = "ZACZNIJ rozwiązywanie sudoku ";
        }
        boardField.putString(actCol, actRow, "F12");
        fluidField.putString(namesCol, actRow, optionName, SGR.BOLD);

        actRow++;
        boardField.putString(actCol, actRow, "ESC");
        fluidField.putString(namesCol, actRow, "Koniec programu");
    }

    public static void drawSolvingInfo(int _startRow, int _startCol) {
        final int colWidth = 24;
        int actRow = _startRow;
        int actCol = _startCol;
        String text;

        boardField.putString(actCol, actRow, "=== INFORMACJE ===");

        actRow++;
        text = SolvingInfo.getSolvingInfoStatus();
        text = String.format("%-" + (colWidth - text.length()) + "s", text);
        fluidField.putString(actCol, actRow, text);

        actRow++;
        text = SolvingInfo.getSolvingInfoTime();
        text = String.format("%-" + (colWidth - text.length()) + "s", text);
        fluidField.putString(actCol, actRow, text);

        actRow++;
        text = SolvingInfo.getSolvingInfoDetails1();
        text = String.format("%-" + (colWidth - text.length()) + "s", text);
        fluidField.putString(actCol, actRow, text);

        actRow++;
        text = SolvingInfo.getSolvingInfoDetails2();
        text = String.format("%-" + (colWidth - text.length()) + "s", text);
        fluidField.putString(actCol, actRow, text);

        actRow++;
        text = SolvingInfo.getSolvingInfoDetails3();
        text = String.format("%-" + (colWidth - text.length()) + "s", text);
        fluidField.putString(actCol, actRow, text);
    }

    public static void setSolvingInfoTime(long _actTime) {
        // long nanoseconds1 = _actTime;
        // long minutes1 = nanoseconds1 / 60_000_000_000L;
        // nanoseconds1 %= 60_000_000_000L;
        // long seconds1 = nanoseconds1 / 1_000_000_000;

        // long nanoseconds2 = MAX_SOLVING_TIME;
        // long minutes2 = nanoseconds2 / 60_000_000_000L;
        // nanoseconds2 %= 60_000_000_000L;
        // long seconds2 = nanoseconds2 / 1_000_000_000;
        // solvingInfoTime = String.format("Czas: %02dm %02ds z %02dm %02ds", minutes1,
        // seconds1, minutes2, seconds2);
    }

    public static void startSolving() {
        switch (solvingMethod) {
            case BRUTE_FORCE_X01:
                // SolverBruteForce solver = new
                // SolverBruteForce(boardForScreen.getBoardData());
                Logger.info("Rozpoczęto rozwiązywanie sudoku metodą Brute Force X01.");
                break;
            case BRUTE_FORCE_X03:
                Logger.info("Rozpoczęto rozwiązywanie sudoku metodą Brute Force X03.");
                break;
            case BRUTE_FORCE_X10:
                Logger.info("Rozpoczęto rozwiązywanie sudoku metodą Brute Force X10.");
                break;
            case GENETIC_X01:
                Logger.info("Rozpoczęto rozwiązywanie sudoku metodą Genetic X01.");
                break;
            case GENETIC_X03:
                Logger.info("Rozpoczęto rozwiązywanie sudoku metodą Genetic X03.");
                break;
            case GENETIC_X10:
                Logger.info("Rozpoczęto rozwiązywanie sudoku metodą Genetic X10.");
                break;
            default:
                Logger.warn("Nieoczekiwana wartość ({})!", solvingMethod);
                break;
        }
    }

    /**
     * ==============================================================================
     * ================================================================MET.DYNAMICZNE
     * ==============================================================================
     */

}
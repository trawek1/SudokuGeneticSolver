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

    private static SolvingMethodsEnum solvingMethod;
    public static SudokuBoardsEnum sudokuTestBoard;
    public static Board boardForScreen;

    private static Terminal terminal = null;
    private static Screen screen = null;
    private static TextGraphics constField;
    private static TextGraphics fluidField;
    private static TextGraphics boardField;
    private static Thread solverThread;

    private static int xxx_counter = 0;
    /**
     * ==============================================================================
     * =================================================================MET.STATYCZNE
     * ==============================================================================
     */

    static {
        sudokuTestBoard = SudokuBoardsEnum.X2E;
        solvingMethod = SolvingMethodsEnum.BRUTE_FORCE_X01;
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
                        SolverGenetic.changeGenerationsNumberByStep();
                    } else if (keyStroke.getKeyType() == KeyType.F12) {
                        startStopSolving();
                    }
                    mustRefresh = true;
                }

                if (SolverInfo.isSolvingTimeExceeded()) {
                    SolverInfo.changeStatusTo(SolvingStatusEnum.STOPPED_BY_TIMEOUT);
                }
                if (SolverInfo.isUpdateTime()) {
                    mustRefresh = true;
                }
                if (SolverInfo.isChangedAndReset()) {
                    mustRefresh = true;
                }
                if (mustRefresh) {
                    mustRefresh = false;
                    drawAll();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Logger.error("Nieoczekiwanie zakończono wątek główny programu! {}", e.getMessage());
                }
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

    public static void startStopSolving() {
        if (SolverInfo.getStatus() == SolvingStatusEnum.IN_PROGRESS) {
            SolverInfo.changeStatusTo(SolvingStatusEnum.STOPPED_BY_USER);
            try {
                solverThread.join(2000);
                if (solverThread.isAlive()) {
                    Logger.warn("Wymuszono zakończenie wątku solvera!");
                    solverThread.interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.error("Nieoczekiwanie zakończono wątek główny programu! {}", e.getMessage());
            }
        }
        if (SolverInfo.getStatus() == SolvingStatusEnum.STARTED) {
            // TODO ? czy to musi być?
        } else {
            SolverInfo.changeStatusTo(SolvingStatusEnum.STARTED);
            solverThread = new Thread(() -> {
                try {
                    startSolving();
                } catch (Exception e) {
                    Logger.error("Nieoczekiwanie zakończono wątek solvera! {}", e.getMessage());
                    SolverInfo.changeStatusTo(SolvingStatusEnum.ERROR);
                }
            });
            solverThread.start();
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
        optionValue = String.format("%-" + (colWidth - optionName.length()) + "d",
                SolverGenetic.getGenerationsNumber());
        boardField.putString(actCol, actRow, "F8");
        fluidField.putString(namesCol, actRow, optionName);
        fluidField.putString(namesCol + optionName.length(), actRow, optionValue, SGR.BOLD);

        actRow++;
        if (SolverInfo.getStatus() == SolvingStatusEnum.IN_PROGRESS) {
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

        // TODO x usunąć po testach
        xxx_counter++;
        boardField.putString(actCol, actRow, "=== INFORMACJE ===" + xxx_counter);

        actRow++;
        text = SolverInfo.getTxtStatus();
        if (text.length() >= colWidth) {
            text = text.substring(0, colWidth - 1);
        }
        text = String.format("%-" + (colWidth - text.length()) + "s", text);
        fluidField.putString(actCol, actRow, text);

        actRow++;
        text = SolverInfo.getTxtTime();
        if (text.length() >= colWidth) {
            text = text.substring(0, colWidth - 1);
        }
        text = String.format("%-" + (colWidth - text.length()) + "s", text);
        fluidField.putString(actCol, actRow, text);

        actRow++;
        text = SolverInfo.getTxtDetails1();
        if (text.length() >= colWidth) {
            text = text.substring(0, colWidth - 1);
        }
        text = String.format("%-" + (colWidth - text.length()) + "s", text);
        fluidField.putString(actCol, actRow, text);

        actRow++;
        text = SolverInfo.getTxtDetails2();
        if (text.length() >= colWidth) {
            text = text.substring(0, colWidth - 1);
        }
        text = String.format("%-" + (colWidth - text.length()) + "s", text);
        fluidField.putString(actCol, actRow, text);

        actRow++;
        text = SolverInfo.getTxtDetails3();
        if (text.length() >= colWidth) {
            text = text.substring(0, colWidth - 1);
        }
        text = String.format("%-" + (colWidth - text.length()) + "s", text);
        fluidField.putString(actCol, actRow, text);
    }

    public static void startSolving() {
        SolverBruteForce solverBruteForce;
        SolverGenetic solverGenetic;
        switch (solvingMethod) {
            case BRUTE_FORCE_X01:
                setSolvingDataFilenameToActual("brute-force-x01");
                solverBruteForce = new SolverBruteForce(boardForScreen.getBoardData());
                SolverBase.setSolvingIterationsLimit(1);
                SolverBase.saveSolvingDataToFile("=== metoda  : " + SudokuSolverGUI.getSolvingMethodName());
                SolverBase.saveSolvingDataToFile("=== plansza : " + SudokuSolverGUI.getSudokuTestBoardName());
                SolverBase.saveSolvingDataToFile("=== czas max: " + convertMilisecondsToHMSV(SOLVING_TIME_MAX, 2));
                SolverBase.saveSolvingDataToFile("=== iteracje: " + SolverBase.getSolvingIterationsLimit());
                solverBruteForce.resetBoard();
                solverBruteForce.startSolving();
                break;
            case BRUTE_FORCE_X03:
                setSolvingDataFilenameToActual("brute-force-x03");
                solverBruteForce = new SolverBruteForce(boardForScreen.getBoardData());
                SolverBase.setSolvingIterationsLimit(3);
                SolverBase.saveSolvingDataToFile("=== metoda  : " + SudokuSolverGUI.getSolvingMethodName());
                SolverBase.saveSolvingDataToFile("=== plansza : " + SudokuSolverGUI.getSudokuTestBoardName());
                SolverBase.saveSolvingDataToFile("=== czas max: " + convertMilisecondsToHMSV(SOLVING_TIME_MAX, 2));
                SolverBase.saveSolvingDataToFile("=== iteracje: " + SolverBase.getSolvingIterationsLimit());
                while (SolverBase.areIterationsInProgress()) {
                    solverBruteForce.resetBoard();
                    solverBruteForce.startSolving();
                }
                resetTimeMeasurement();
                break;
            case BRUTE_FORCE_X10:
                setSolvingDataFilenameToActual("brute-force-x10");
                solverBruteForce = new SolverBruteForce(boardForScreen.getBoardData());
                SolverBase.setSolvingIterationsLimit(10);
                SolverBase.saveSolvingDataToFile("=== metoda  : " + SudokuSolverGUI.getSolvingMethodName());
                SolverBase.saveSolvingDataToFile("=== plansza : " + SudokuSolverGUI.getSudokuTestBoardName());
                SolverBase.saveSolvingDataToFile("=== czas max: " + convertMilisecondsToHMSV(SOLVING_TIME_MAX, 2));
                SolverBase.saveSolvingDataToFile("=== iteracje: " + SolverBase.getSolvingIterationsLimit());
                while (SolverBase.areIterationsInProgress()) {
                    solverBruteForce.resetBoard();
                    solverBruteForce.startSolving();
                }
                resetTimeMeasurement();
                break;
            case GENETIC_X01:
                setSolvingDataFilenameToActual("genetic-x01");
                solverGenetic = new SolverGenetic(boardForScreen.getBoardData());
                SolverBase.setSolvingIterationsLimit(1);
                SolverBase.saveSolvingDataToFile("=== metoda               : "
                        + SudokuSolverGUI.getSolvingMethodName());
                SolverBase.saveSolvingDataToFile("=== plansza              : "
                        + SudokuSolverGUI.getSudokuTestBoardName());
                SolverBase.saveSolvingDataToFile("=== czas max             : "
                        + convertMilisecondsToHMSV(SOLVING_TIME_MAX, 2));
                SolverBase.saveSolvingDataToFile("=== iteracje             : "
                        + SolverBase.getSolvingIterationsLimit());
                SolverBase.saveSolvingDataToFile("=== generator rodziców   : "
                        + SolverGeneticParentMaker.getParentGeneratingMethodName());
                SolverBase.saveSolvingDataToFile("=== funkcja dopasowania  : "
                        + SolverGeneticIndividual.getFitnessCalculatingMethodName());
                SolverBase.saveSolvingDataToFile("=== metoda krzyżowania   : "
                        + SolverGeneticCrossover.getCrossoverMethodName());
                SolverBase.saveSolvingDataToFile("=== selekcja rodziców    : "
                        + SolverGeneticPopulation.getParentSelectionMethodName());
                SolverBase.saveSolvingDataToFile("=== rozmiar populacji    : "
                        + SolverGeneticPopulation.getPopulationSize() + " ("
                        + SolverGeneticPopulation.POPULATION_SIZE_MIN + " - "
                        + SolverGeneticPopulation.POPULATION_SIZE_MAX + ")");
                SolverBase.saveSolvingDataToFile("=== rozmiar puli rodziców: "
                        + SolverGeneticPopulation.getBestParentsPercent() + "% ("
                        + SolverGeneticPopulation.BEST_PARENTS_PERCENT_MIN + "% - "
                        + SolverGeneticPopulation.BEST_PARENTS_PERCENT_MAX + "%) / "
                        + SolverGeneticPopulation.getNumberOfParents() + "x");
                while (SolverBase.areIterationsInProgress()) {
                    solverGenetic.startSolving();
                }
                resetTimeMeasurement();
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

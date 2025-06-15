import java.time.LocalDateTime;

public class SolverGenetic extends SolverBase {
    public static final int GENERATIONS_NUMBER_MIN = 5_000;
    public static final int GENERATIONS_NUMBER_MAX = 500_000;
    private static final int GENERATIONS_NUMBER_STEP = 5_000;
    private static final int GENERATIONS_NUMBER_DEFAULT = 50_000;
    private static int generationsNumber;

    private int[] boardData;

    static {
        generationsNumber = GENERATIONS_NUMBER_DEFAULT;
    }

    public SolverGenetic(int[] _boardData) {
        this.boardData = _boardData;
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

    public void saveToFileMethodParameters() {
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
        SolverBase.saveSolvingDataToFile("=== ilość generacji      : "
                + SolverGenetic.getGenerationsNumber() + " ("
                + SolverGenetic.GENERATIONS_NUMBER_MIN + " - "
                + SolverGenetic.GENERATIONS_NUMBER_MAX + ")");
    }

    private boolean solve() {
        SolverGeneticPopulation population = new SolverGeneticPopulation(this.boardData);
        population.savePopulationData();

        for (int i = 0; i < generationsNumber; i++) {
            if (SolverInfo.getStatus() != SolvingStatusEnum.IN_PROGRESS) {
                break;
            }
            if (population.isSolved()) {
                break;
            }
            population.createNextAndSwapPopulation();
            population.savePopulationData();
        }

        return population.isSolved();
    }

    public void startSolving() {
        SolverBase.saveSolvingDataToFile("=== start                : " + LocalDateTime.now());
        startTimeMeasurement();
        boolean isSolved = this.solve();
        stopTimeMeasurement();
        SolverBase.saveSolvingDataToFile("=== stop                 : " + LocalDateTime.now());

        if (SolverInfo.getStatus() == SolvingStatusEnum.IN_PROGRESS) {
            if (isSolved) {
                if (SolverBase.areIterationsInProgress()) {
                    SolverInfo.setStatusTo(SolvingStatusEnum.ITERATION_COMPLETED);
                } else {
                    SolverInfo.setStatusTo(SolvingStatusEnum.COMPLETED);
                }
                SolverBase.saveSolvingDataToFile("=== czas rozwiazania: " + showSolvingAverageTime());
            } else {
                SolverInfo.setStatusTo(SolvingStatusEnum.STOPPED_BY_GENERATIONS_LIMIT);
            }
        }
    }
}

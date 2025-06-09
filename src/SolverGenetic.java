import java.time.LocalDateTime;

public class SolverGenetic extends SolverBase {
    private static final int GENERATIONS_NUMBER_MIN = 5_000;
    private static final int GENERATIONS_NUMBER_MAX = 500_000;
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

    private boolean solve() {
        SolverGeneticPopulation population = null;

        for (int i = 0; i < generationsNumber; i++) {
            if (population == null) {
                population = new SolverGeneticPopulation(this.boardData);
                // TODO >>>> dodać sprawdzanie fitness=0
                // TODO >>>> przenieść tu zapis statystyk do pliku
            }
            population.createNextAndSwapPopulation();
            // TODO >>>> przenieść tu zapis statystyk do pliku
            // TODO >>>> dodać sprawdzanie fitness=0
        }

        return true; // TODO >>>>> uzupełnić wyłapywanie fitness = 0
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
                    SolverInfo.changeStatusTo(SolvingStatusEnum.ITERATION_COMPLETED);
                } else {
                    SolverInfo.changeStatusTo(SolvingStatusEnum.COMPLETED);
                }
                SolverBase.saveSolvingDataToFile("=== czas rozwiazania: " + showSolvingAverageTime());
            } else {
                SolverInfo.changeStatusTo(SolvingStatusEnum.FAILED);
            }
        }
    }
}

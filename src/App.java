import org.tinylog.Logger;
// import java.util.Arrays;

public class App {
        public static void main(String[] args) {
                System.out.println("==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== === START");
                Logger.debug("==== ==== ==== ==== START");

                // ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- Solver Brute Force
                // SolverBruteForce solver = new
                // SolverBruteForce(BoardBase.BOARD_TEST_DATA_4x4);
                // solver.startSolvingManyTimes();

                // ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- Solver Genetic Population
                SolverGeneticParentMaker.saveSolvingPreferencesToFile();
                SolverGeneticIndividual.saveSolvingPreferencesToFile();
                SolverGeneticCrossover.saveSolvingPreferencesToFile();
                SolverGeneticPopulation.saveSolvingPreferencesToFile();

                System.out.println("Populacja=" + SolverGeneticPopulation.getPopulationSize()
                                + ", % rodziców=" + SolverGeneticPopulation.getBestParentsPercent()
                                + ", pozycja ostatniego=" + SolverGeneticPopulation.getNumberOfParents());
                SolverBase.resetTimeMeasurement();
                SolverBase.startTimeMeasurement();
                SolverGeneticPopulation population = new SolverGeneticPopulation(BoardBase.BOARD_TEST_DATA_5x5);
                int firstMax = population.getStatsBestFitness();
                int intimeMax = firstMax;
                System.out.println("Generacja= " + population.getGenerationsCount()
                                + " :: Dopasowanie: max=" + population.getStatsBestFitness()
                                + ", ost.rodzica=" + population.getStatsFitnessOfLastParent()
                                + ", med=" + population.getStatsAverageFitness()
                                + ", min=" + population.getStatsWorstFitness());
                for (int i = 0; i < 50_000; i++) {
                        population.createNextAndSwapPopulation();
                        if (population.getGenerationsCount() % 1000 == 0) {
                                System.out.println("Generacja= " + population.getGenerationsCount()
                                                + " :: Dopasowanie: max=" + population.getStatsBestFitness()
                                                + ", ost.rodzica=" + population.getStatsFitnessOfLastParent()
                                                + ", med=" + population.getStatsAverageFitness()
                                                + ", min=" + population.getStatsWorstFitness());
                                if (population.getStatsBestFitness() < intimeMax) {
                                        intimeMax = population.getStatsBestFitness();
                                }
                        }
                }
                SolverBase.stopTimeMeasurement();

                Logger.info("Czas rozwiązywania: {}", SolverBase.showSolvingTime());
                System.out.println("RESULT MAX: first=" + firstMax
                                + ", intime=" + intimeMax
                                + ", last=" + population.getStatsBestFitness());

                // ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- Solver Genetic Individual
                // SolverGeneticIndividual mother = new
                // SolverGeneticIndividual(BoardBase.BOARD_TEST_DATA_3x3);
                // mother.xxx_showBoard();
                // mother.calculateFitness();
                // System.out.println("Fitness: " + mother.getBoardErrorLevel());

                // mother.getBoardFromParentHard();
                // mother.xxx_showBoard();
                // mother.calculateFitness();
                // System.out.println("Fitness: " + mother.getBoardErrorLevel());

                // mother.getBoardFromParentSoft();
                // mother.xxx_showBoard();
                // mother.calculateFitness();
                // System.out.println("Fitness: " + mother.getBoardErrorLevel());

                // ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- Solver Genetic Crossover
                // SolverGeneticCrossover crossover = new
                // SolverGeneticCrossover(BoardBase.BOARD_TEST_DATA_3x3,
                // BoardBase.BOARD_TEST_DATA_3x3);
                // crossover.crossoverUniform();
                // crossover.crossoverUniformBalanced();
                // crossover.crossoverSinglePoint(SolverGeneticCrossover.USE_RANDOM_POINT);
                // crossover.crossoverTwoPoint(SolverGeneticCrossover.USE_SYMMETRIC_POINT,
                // SolverGeneticCrossover.USE_SYMMETRIC_POINT);

                // ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- Board Field Possibilities
                // BoardFieldPossibilities options = new BoardFieldPossibilities(3);
                // options.setAllValuesAsPossible();
                // System.out.println("Possible values: " +
                // Arrays.toString(options.getValuesPossible()));
                // System.out.println("Impossible values: " +
                // Arrays.toString(options.getValuesImpossible()));
                // System.out.println("Unknown values: " +
                // Arrays.toString(options.getValuesUnknown()));
                // options.setValueAsImpossible(2);

                Logger.debug("==== ==== ==== ==== STOP");
                System.out.println("==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== STOP");
        }
}

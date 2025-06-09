import org.tinylog.Logger;

public class App {
        public static void main(String[] args) {
                System.out.println("==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== === START");
                Logger.debug("==== ==== ==== ==== START");

                // ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- Solver Brute Force
                // SolverBruteForce solver = new
                // SolverBruteForce(SolverStarter.sudokuTestBoard.getBoardData());
                // solver.startSolvingManyTimes();

                // ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- Solver Genetic Population
                // SolverGeneticParentMaker.saveSolvingPreferencesToFile();
                // SolverGeneticIndividual.saveSolvingPreferencesToFile();
                // SolverGeneticCrossover.saveSolvingPreferencesToFile();
                // SolverGeneticPopulation.saveSolvingPreferencesToFile();

                // System.out.println("Populacja=" + SolverGeneticPopulation.getPopulationSize()
                // + ", % rodziców=" + SolverGeneticPopulation.getBestParentsPercent()
                // + ", pozycja ostatniego=" + SolverGeneticPopulation.getNumberOfParents());
                // SolverBase.resetTimeMeasurement();
                // SolverBase.startTimeMeasurement();
                // SolverGeneticPopulation population = new SolverGeneticPopulation(
                // SudokuSolverGUI.sudokuTestBoard.getBoardData());
                // int firstMax = population.getStatsBestFitness();
                // int intimeMax = firstMax;
                // System.out.println("Generacja= " + population.getGenerationsCount()
                // + " :: Dopasowanie: max=" + population.getStatsBestFitness()
                // + ", ost.rodzica=" + population.getStatsFitnessOfLastParent()
                // + ", med=" + population.getStatsAverageFitness()
                // + ", min=" + population.getStatsWorstFitness());

                // for (int i = 0; i < 50_000; i++) {
                // population.createNextAndSwapPopulation();
                // if (population.getGenerationsCount() % 1000 == 0) {
                // System.out.println("Generacja= " + population.getGenerationsCount()
                // + " :: Dopasowanie: max=" + population.getStatsBestFitness()
                // + ", ost.rodzica=" + population.getStatsFitnessOfLastParent()
                // + ", med=" + population.getStatsAverageFitness()
                // + ", min=" + population.getStatsWorstFitness());
                // if (population.getStatsBestFitness() < intimeMax) {
                // intimeMax = population.getStatsBestFitness();
                // }
                // }
                // }
                // SolverBase.stopTimeMeasurement();

                // Logger.info("Czas rozwiązywania: {}", SolverBase.showSolvingTime());
                // System.out.println("RESULT MAX: first=" + firstMax
                // + ", intime=" + intimeMax
                // + ", last=" + population.getStatsBestFitness());

                Logger.debug("==== ==== ==== ==== STOP");
                System.out.println("==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== STOP");
        }
}

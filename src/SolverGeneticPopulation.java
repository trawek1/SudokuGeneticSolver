import java.text.SimpleDateFormat;
import java.util.*;

import org.tinylog.Logger;

public class SolverGeneticPopulation extends SolverBase {
    public static final int FITNESS_LEVEL_ERROR = -1;

    public static final int POPULATION_SIZE_MIN = 50;
    public static final int POPULATION_SIZE_MAX = 500;
    private static final int POPULATION_SIZE_STEP = 50;
    private static final int POPULATION_SIZE_DEFAULT = 100;
    private static int populationSize;

    public static final int BEST_PARENTS_PERCENT_MIN = 2;
    public static final int BEST_PARENTS_PERCENT_MAX = 50;
    private static final int BEST_PARENTS_PERCENT_STEP = 2;
    private static final int BEST_PARENTS_PERCENT_DEFAULT = 30;
    private static int bestParentsPercent;

    private static final ParentSelectionMethodsEnum PARENT_SELECTION_METHOD_DEFAULT = ParentSelectionMethodsEnum.ROULETTE_SELECTION;
    private static ParentSelectionMethodsEnum parentSelectionMethod;

    private List<SolverGeneticIndividual> population;
    private int[] initialBoard;
    private boolean solved;
    private int generationsCount;

    /**
     * ==============================================================================
     * =================================================================MET.STATYCZNE
     * ==============================================================================
     */

    static {
        parentSelectionMethod = PARENT_SELECTION_METHOD_DEFAULT;
        populationSize = POPULATION_SIZE_DEFAULT;
        bestParentsPercent = BEST_PARENTS_PERCENT_DEFAULT;
    }

    public static int getPopulationSize() {
        return populationSize;
    }

    public static void changePopulationSizeByStep() {
        populationSize += POPULATION_SIZE_STEP;
        if (populationSize > POPULATION_SIZE_MAX) {
            populationSize = POPULATION_SIZE_MIN;
        }
    }

    public static void setPopulationSize(int _size) {
        if (_size < POPULATION_SIZE_MIN && _size > POPULATION_SIZE_MAX) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano {}-{}, otrzymano {}. Ustawiono wartość domyślną {}.",
                    POPULATION_SIZE_MIN, POPULATION_SIZE_MAX, _size, POPULATION_SIZE_DEFAULT);
            populationSize = POPULATION_SIZE_DEFAULT;
        }
        populationSize = _size;
    }

    public static int getBestParentsPercent() {
        return bestParentsPercent;
    }

    public static void changeBestParentsPercentByStep() {
        bestParentsPercent += BEST_PARENTS_PERCENT_STEP;
        if (bestParentsPercent > BEST_PARENTS_PERCENT_MAX) {
            bestParentsPercent = BEST_PARENTS_PERCENT_MIN;
        }
    }

    public static void setBestParentsPercent(int _percent) {
        if (_percent < BEST_PARENTS_PERCENT_MIN && _percent > BEST_PARENTS_PERCENT_MAX) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano {}-{}, otrzymano {}. Ustawiono wartość domyślną {}.",
                    BEST_PARENTS_PERCENT_MIN, BEST_PARENTS_PERCENT_MAX, _percent, BEST_PARENTS_PERCENT_DEFAULT);
            bestParentsPercent = BEST_PARENTS_PERCENT_DEFAULT;
        }
        bestParentsPercent = _percent;
    }

    public static int getNumberOfParents() {
        int parentsAmount = (int) Math.ceil(
                populationSize * (getBestParentsPercent() / 100.0));
        return (parentsAmount > 0 ? parentsAmount : 1);
    }

    public static String getParentSelectionMethodName() {
        return parentSelectionMethod.getDisplayName();
    }

    public static void changeParentSelectionMethod() {
        parentSelectionMethod = parentSelectionMethod.next();
    }

    /**
     * ==============================================================================
     * ================================================================MET.DYNAMICZNE
     * ==============================================================================
     */

    public SolverGeneticPopulation(int[] _initialBoard) {
        this.initialBoard = _initialBoard;
        this.population = new ArrayList<>(getPopulationSize());
        this.solved = false;
        this.generationsCount = 0;
        this.initializePopulation();
    }

    private void initializePopulation() {
        for (int i = 0; i < getPopulationSize(); i++) {
            if (SolverInfo.getStatus() != SolvingStatusEnum.IN_PROGRESS) {
                break;
            }
            SolverGeneticIndividual individual = new SolverGeneticIndividual(this.initialBoard);
            individual.getBoardFromParent();
            this.population.add(individual);
            if (individual.getFitnessLevel() == 0) {
                this.solved = true;
                break;
            }
        }
        this.sortPopulationByFitness();
    }

    public boolean isSolved() {
        return this.solved;
    }

    private void sortPopulationByFitness() {
        this.population.sort(Comparator.comparingInt(SolverGeneticIndividual::getFitnessLevel));
    }

    public int getGenerationsCount() {
        return generationsCount;
    }

    public int getStatsBestFitness() {
        if (population.get(0) != null) {
            return population.get(0).getFitnessLevel();
        }
        return FITNESS_LEVEL_ERROR;
    }

    public int getStatsWorstFitness() {
        return population.get(population.size() - 1).getFitnessLevel();
    }

    public int getStatsFitnessOfLastParent() {
        int lastParentPosition = getNumberOfParents() - 1;
        if (population.size() > lastParentPosition) {
            return population.get(lastParentPosition).getFitnessLevel();
        }
        return FITNESS_LEVEL_ERROR;
    }

    public double getStatsAverageFitness() {
        double averageFitness = population.stream()
                .mapToInt(SolverGeneticIndividual::getFitnessLevel)
                .average()
                .orElse(0.0);
        return Math.round(averageFitness * 100.0) / 100.0;
    }

    public List<SolverGeneticIndividual> getParentsByEliteSelection() {
        List<SolverGeneticIndividual> parents = new ArrayList<>();

        for (int i = 0; i < getNumberOfParents(); i++) {
            parents.add(population.get(i));
        }
        return parents;
    }

    public List<SolverGeneticIndividual> getParentsByRouletteSelection() {
        Random random = new Random();

        int parentSize = getNumberOfParents();
        List<SolverGeneticIndividual> parents = new ArrayList<>();

        double totalFitnessInverse = this.population.stream()
                .mapToDouble(individual -> 1.0 / (1.0 + individual.getFitnessLevel()))
                .sum();

        parents.add(this.population.get(0));
        while (parents.size() < parentSize) {
            double randomValue = random.nextDouble() * totalFitnessInverse;
            double cumulativeProbability = 0.0;
            int index = 0;
            while (cumulativeProbability < randomValue) {
                cumulativeProbability += 1.0 / (1.0 + this.population.get(index).getFitnessLevel())
                        / totalFitnessInverse;
                if (cumulativeProbability >= randomValue) {
                    parents.add(this.population.get(index));
                    break;
                }
                index = (index < this.population.size() - 1) ? index + 1 : 0;
            }
        }
        return parents;
    }

    public void createNextAndSwapPopulation() {
        Random random = new Random();

        List<SolverGeneticIndividual> parents;
        switch (parentSelectionMethod) {
            case ELITE_SELECTION:
                parents = this.getParentsByEliteSelection();
                break;
            case ROULETTE_SELECTION:
                parents = this.getParentsByRouletteSelection();
                break;
            default:
                Logger.error("Nieznana metoda selekcji rodziców: {}", parentSelectionMethod);
                parents = this.getParentsByRouletteSelection();
        }

        this.population.clear();
        this.generationsCount++;
        for (int i = 0; i < getPopulationSize(); i++) {
            if (SolverInfo.getStatus() != SolvingStatusEnum.IN_PROGRESS) {
                break;
            }

            int parent1Position = random.nextInt(parents.size());
            SolverGeneticIndividual parent1 = parents.get(parent1Position);
            int parent2Position = random.nextInt(parents.size());
            SolverGeneticIndividual parent2 = parents.get(parent2Position);

            SolverGeneticCrossover crossover = new SolverGeneticCrossover(parent1.getBoardData(),
                    parent2.getBoardData());
            SolverGeneticIndividual child = new SolverGeneticIndividual(crossover.getCrossover());
            child.calculateFitness();
            population.add(child);
            // saveSolvingDataToFile("GEN, POKOLENIE nr=," + this.generationsCount
            // + ", POTOMEK: nr=," + (i + 1)
            // + ", lvl=," + child.getBoardErrorLevel()
            // + ", RODZIC-1: pos=," + parent1Position
            // + ", lvl=," + parent1.getBoardErrorLevel()
            // + ", RODZIC-2 pos=," + parent2Position
            // + ", lvl=," + parent2.getBoardErrorLevel());
            if (child.getFitnessLevel() == 0) {
                this.solved = true;
                break;
            }
        }
        this.sortPopulationByFitness();
    }

    public void savePopulationData() {
        SolverInfo.addDetails(String.format("GEN %9d", this.generationsCount) + this.getStatData());

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String time = (", TIME," + sdf.format(date));

        String stats = ", STAT," + this.getStatData();

        String fitnessAll = ", FIT,";
        for (SolverGeneticIndividual individual : this.population) {
            fitnessAll += individual.getFitnessLevel() + ",";
        }
        saveSolvingDataToFile(String.format("GEN %9d", this.generationsCount) + time + stats + fitnessAll);
    }

    public String getStatData() {
        return this.getStatsBestFitness() + ","
                + this.getStatsFitnessOfLastParent() + ","
                + String.format("%.0f", this.getStatsAverageFitness()) + ","
                + this.getStatsWorstFitness();

    }
}
import java.util.*;

public class SolverGeneticPopulation extends SolverGeneticBase {

    private List<SolverGeneticIndividual> population;
    private int[] initialBoard;
    private int generationsCount;

    public SolverGeneticPopulation(int[] _initialBoard) {
        this.initialBoard = _initialBoard;
        this.population = new ArrayList<>(this.getPopulationSize());
        this.initializePopulation();
        this.sortPopulationByFitness();
        this.generationsCount = 1;
    }

    private void initializePopulation() {
        for (int i = 0; i < this.getPopulationSize(); i++) {
            SolverGeneticIndividual individual = new SolverGeneticIndividual(this.initialBoard);
            individual.getBoardFromParent();
            this.population.add(individual);
        }
    }

    private void calculateFitnessForAll() {
        for (SolverGeneticIndividual individual : this.population) {
            individual.calculateFitnessByMissingValues();
        }
    }

    private void sortPopulationByFitness() {
        this.population.sort(Comparator.comparingInt(SolverGeneticIndividual::getBoardErrorLevel));
    }

    public int getGenerationsCount() {
        return generationsCount;
    }

    public int getStatsBestFitness() {
        return population.get(0).getBoardErrorLevel();
    }

    public int getStatsWorstFitness() {
        return population.get(population.size() - 1).getBoardErrorLevel();
    }

    public int getStatsFitnessOfLastParent() {
        return population.get(this.getCountOfParents() - 1).getBoardErrorLevel();
    }

    public double getStatsAverageFitness() {
        double averageFitness = population.stream()
                .mapToInt(SolverGeneticIndividual::getBoardErrorLevel)
                .average()
                .orElse(0.0);
        return Math.round(averageFitness * 100.0) / 100.0;
    }

    public List<SolverGeneticIndividual> getParentsByEliteSelection() {
        List<SolverGeneticIndividual> parents = new ArrayList<>();

        for (int i = 0; i < this.getCountOfParents(); i++) {
            parents.add(population.get(i));
        }
        return parents;
    }

    public List<SolverGeneticIndividual> getParentsByRouletteSelection() {
        Random random = new Random();

        int parentSize = this.getCountOfParents();
        List<SolverGeneticIndividual> parents = new ArrayList<>();

        double totalFitnessInverse = this.population.stream()
                .mapToDouble(individual -> 1.0 / (1.0 + individual.getBoardErrorLevel()))
                .sum();

        for (int i = 0; i < parentSize; i++) {
            double randomValue = random.nextDouble() * totalFitnessInverse;
            double cumulativeProbability = 0.0;
            int index = 0;
            while (cumulativeProbability < randomValue) {
                cumulativeProbability += 1.0 / (1.0 + this.population.get(index).getBoardErrorLevel())
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

    // TODO >>>>> dopisać metodę turniejową
    // TODO >>>>> dopisać zachowanie elity w każdej metodzie

    public void createNextAndSwapPopulation() {
        Random random = new Random();

        // TODO >>>>> wybrać metodę doboru rodziców
        List<SolverGeneticIndividual> parents = this.getParentsByRouletteSelection();

        this.population.clear();
        for (int i = 0; i < this.getPopulationSize(); i++) {
            SolverGeneticIndividual parent1 = parents.get(random.nextInt(parents.size() - 1));
            SolverGeneticIndividual parent2 = parents.get(random.nextInt(parents.size() - 1));

            SolverGeneticCrossover crossover = new SolverGeneticCrossover(parent1.getBoardData(),
                    parent2.getBoardData());
            SolverGeneticIndividual child = new SolverGeneticIndividual(crossover.getCrossover());
            population.add(child);
        }
        this.calculateFitnessForAll();
        this.sortPopulationByFitness();

        this.generationsCount++;
    }

}
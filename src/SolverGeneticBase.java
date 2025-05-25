import org.tinylog.Logger;

public class SolverGeneticBase extends SolverBase {
    public static final int POPULATION_SIZE_MIN = 50;
    public static final int POPULATION_SIZE_MAX = 500;
    public static final int POPULATION_SIZE_DEFAULT = 500;
    public static final int BEST_PARENTS_PERCENT_MIN = 1;
    public static final int BEST_PARENTS_PERCENT_MAX = 50;
    public static final int BEST_PARENTS_PERCENT_DEFAULT = 5;
    public static final int MUTATION_PROBABILITY_MIN = 0;
    public static final int MUTATION_PROBABILITY_MAX = 100;
    public static final int MUTATION_PROBABILITY_DEFAULT = 10;
    private int populationSize;
    private int bestParentsPercent;
    private int mutationProbability;

    public SolverGeneticBase() {
        this.populationSize = POPULATION_SIZE_DEFAULT;
        this.bestParentsPercent = BEST_PARENTS_PERCENT_DEFAULT;
        this.mutationProbability = MUTATION_PROBABILITY_DEFAULT;
    }

    public int getPopulationSize() {
        return this.populationSize;
    }

    public void setPopulationSize(int _size) {
        if (_size < POPULATION_SIZE_MIN && _size > POPULATION_SIZE_MAX) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano {}-{}, otrzymano {}. Ustawiono wartość domyślną {}.",
                    POPULATION_SIZE_MIN, POPULATION_SIZE_MAX, _size, POPULATION_SIZE_DEFAULT);
            this.populationSize = POPULATION_SIZE_DEFAULT;
        }
        this.populationSize = _size;
    }

    public int getBestParentsPercent() {
        return this.bestParentsPercent;
    }

    public void setBestParentsPercent(int _percent) {
        if (_percent < BEST_PARENTS_PERCENT_MIN && _percent > BEST_PARENTS_PERCENT_MAX) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano {}-{}, otrzymano {}. Ustawiono wartość domyślną {}.",
                    BEST_PARENTS_PERCENT_MIN, BEST_PARENTS_PERCENT_MAX, _percent, BEST_PARENTS_PERCENT_DEFAULT);
            this.bestParentsPercent = BEST_PARENTS_PERCENT_DEFAULT;
        }
        this.bestParentsPercent = _percent;
    }

    public int getCountOfParents() {
        return (int) Math.ceil(this.populationSize * (this.bestParentsPercent / 100.0)) - 1;
    }

    public int getMutationProbability() {
        return this.mutationProbability;
    }

    public void setMutationProbability(int _probability) {
        if (_probability < MUTATION_PROBABILITY_MIN || _probability > MUTATION_PROBABILITY_MAX) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano {}-{}, otrzymano {}. Ustawiono wartość domyślną {}.",
                    MUTATION_PROBABILITY_MIN, MUTATION_PROBABILITY_MAX, _probability, MUTATION_PROBABILITY_DEFAULT);
            this.mutationProbability = MUTATION_PROBABILITY_DEFAULT;
        } else {
            this.mutationProbability = _probability;
        }
    }
}

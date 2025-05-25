import org.tinylog.Logger;

import java.util.Random;

/**
 * UWAGI:
 * 1) przesyłane w formie jednowymiarowej tablicy dane planszy zawierają pola o
 * wartości ujemnej, które oznaczają pola stałe.
 * 2) oboje rodzice powinni mieć taki sam rozkład pól stałych - nie jest to
 * sprawdzane.
 * 3) pola o wartości 0 są polami pustymi.
 */

public class SolverGeneticCrossover extends SolverGeneticBase {
    public static enum CROSSOVER_METHODS {
        CROSSOVER_UNIFORM, CROSSOVER_BALANCED_UNIFORM, CROSSOVER_SINGLE_POINT, CROSSOVER_TWO_POINTS
    };

    public static final String CROSSOVER_UNIFORM = "Krzyżowanie losowe";
    public static final String CROSSOVER_BALANCED_UNIFORM = "Krzyżowanie losowe balansowane";
    public static final String CROSSOVER_SINGLE_POINT = "Krzyżowanie jedno-punktowe";
    public static final String CROSSOVER_TWO_POINTS = "Krzyżowanie dwu-punktowe";
    public static final String CROSSOVER_DEFAULT = CROSSOVER_SINGLE_POINT;
    public static final int USE_RANDOM_POINT = -1;
    public static final int USE_SYMMETRIC_POINT = -2;
    private static final int RANDOM_BOOLEAN_BALANCE_MIN = 1;
    private static final int RANDOM_BOOLEAN_BALANCE_MAX = 20;
    private final int[] motherBoardData;
    private final int[] fatherBoardData;
    private final int boardLength;
    private CROSSOVER_METHODS crossoverMethod;
    private int randomBooleanBalance;
    private int[] childBoardData;

    public SolverGeneticCrossover(int[] _parent1BoardData, int[] _parent2BoardData) {
        this.motherBoardData = _parent1BoardData;
        this.fatherBoardData = _parent2BoardData;
        this.boardLength = this.motherBoardData.length;
        this.childBoardData = new int[this.boardLength];
        this.randomBooleanBalance = (RANDOM_BOOLEAN_BALANCE_MIN
                + RANDOM_BOOLEAN_BALANCE_MAX) / 2;
        crossoverMethod = CROSSOVER_METHODS.CROSSOVER_SINGLE_POINT;
    }

    private int getRandomPoint() {
        Random random = new Random();
        int crossoverPoint;
        do {
            crossoverPoint = random.nextInt(this.boardLength);
        } while (crossoverPoint <= 0 || crossoverPoint >= this.boardLength);
        return crossoverPoint;
    }

    private int getSymmetricSinglePoint() {
        return this.boardLength / 2;
    }

    private int getSymmetricTwoPointLeft() {
        return this.boardLength / 3;
    }

    private int getSymmetricTwoPointRight() {
        return this.boardLength / 3 * 2;
    }

    private boolean isCrossoverPointNotInRange(int _crossoverPoint) {
        return _crossoverPoint <= 0 || _crossoverPoint >= this.boardLength - 1;
    }

    public String getCrossoverMethodName() {
        return this.crossoverMethod.name();
    }

    public void setCrossoverMethod(CROSSOVER_METHODS _crossoverMethod) {
        this.crossoverMethod = _crossoverMethod;
    }

    public int[] getCrossover() {
        int[] boardData;
        switch (this.crossoverMethod) {
            case CROSSOVER_UNIFORM:
                boardData = this.crossoverUniform();
                break;
            case CROSSOVER_BALANCED_UNIFORM:
                boardData = this.crossoverUniformBalanced();
                break;
            case CROSSOVER_SINGLE_POINT:
                boardData = this.crossoverSinglePoint(USE_RANDOM_POINT);
                break;
            case CROSSOVER_TWO_POINTS:
                boardData = this.crossoverTwoPoint(USE_RANDOM_POINT, USE_RANDOM_POINT);
            default:
                Logger.warn("Nieznana metoda krzyżowania: {}. Używam domyślnej metody {}.",
                        this.crossoverMethod.name(), CROSSOVER_DEFAULT);
                boardData = this.crossoverTwoPoint(USE_RANDOM_POINT, USE_RANDOM_POINT);
        }
        boardData = this.makeMutations(boardData);
        return boardData;
    }

    public int[] crossoverUniform() {
        Random random = new Random();

        for (int i = 0; i < this.boardLength; i++) {
            this.childBoardData[i] = random.nextBoolean() ? this.motherBoardData[i] : this.fatherBoardData[i];
        }
        return this.childBoardData;
    }

    private boolean getRandomBalancedBoolean() {
        if (this.randomBooleanBalance < RANDOM_BOOLEAN_BALANCE_MIN) {
            this.randomBooleanBalance = RANDOM_BOOLEAN_BALANCE_MIN;
        }
        if (this.randomBooleanBalance > RANDOM_BOOLEAN_BALANCE_MAX) {
            this.randomBooleanBalance = RANDOM_BOOLEAN_BALANCE_MAX;
        }

        Random random = new Random();
        int randomNumber = random.nextInt(RANDOM_BOOLEAN_BALANCE_MAX) + 1;
        if (randomNumber <= this.randomBooleanBalance) {
            this.randomBooleanBalance--;
            return false;
        } else {
            this.randomBooleanBalance++;
            return true;
        }
    }

    private int[] crossoverUniformBalanced() {
        for (int i = 0; i < this.boardLength; i++) {
            this.childBoardData[i] = this.getRandomBalancedBoolean()
                    ? this.motherBoardData[i]
                    : this.fatherBoardData[i];
        }
        return this.childBoardData;
    }

    private int[] crossoverSinglePoint(int _crossoverPoint) {
        if (_crossoverPoint == USE_RANDOM_POINT) {
            _crossoverPoint = getRandomPoint();
        } else if (_crossoverPoint == USE_SYMMETRIC_POINT) {
            _crossoverPoint = getSymmetricSinglePoint();
        } else if (this.isCrossoverPointNotInRange(_crossoverPoint)) {
            Logger.warn(
                    "Punkt krzyżowania jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}. Używam punktu w 1/2.",
                    this.boardLength - 2, _crossoverPoint);
            _crossoverPoint = getSymmetricSinglePoint();
        }

        for (int i = 0; i < this.boardLength; i++) {
            this.childBoardData[i] = (i < _crossoverPoint) ? this.motherBoardData[i] : this.fatherBoardData[i];
        }
        return this.childBoardData;
    }

    private int[] crossoverTwoPoint(int _leftCrossoverPoint, int _rightCrossoverPoint) {
        if (_leftCrossoverPoint == USE_RANDOM_POINT) {
            _leftCrossoverPoint = getRandomPoint();
        } else if (_leftCrossoverPoint == USE_SYMMETRIC_POINT) {
            _leftCrossoverPoint = getSymmetricTwoPointLeft();
        } else if (this.isCrossoverPointNotInRange(_leftCrossoverPoint)) {
            Logger.warn(
                    "Lewy punkt krzyżowania jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}. Używam punktu w 1/3.",
                    this.boardLength - 2, _leftCrossoverPoint);
            _leftCrossoverPoint = getSymmetricTwoPointLeft();
        }
        if (_rightCrossoverPoint == USE_RANDOM_POINT) {
            _rightCrossoverPoint = getRandomPoint();
        } else if (_rightCrossoverPoint == USE_SYMMETRIC_POINT) {
            _rightCrossoverPoint = getSymmetricTwoPointRight();
        } else if (this.isCrossoverPointNotInRange(_rightCrossoverPoint)) {
            Logger.warn(
                    "Prawy punkt krzyżowania jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}. Używam punktu w 2/3.",
                    this.boardLength - 2, _rightCrossoverPoint);
            _rightCrossoverPoint = getSymmetricTwoPointRight();
        }
        if (_leftCrossoverPoint == _rightCrossoverPoint) {
            Logger.warn("Lewy punkt krzyżowania jest równy prawemu punktowi krzyżowania! Używam punktów w 1/3 i 2/3.");
            _leftCrossoverPoint = getSymmetricTwoPointLeft();
            _rightCrossoverPoint = getSymmetricTwoPointRight();
        }
        if (_leftCrossoverPoint > _rightCrossoverPoint) {
            Logger.debug("Lewy punkt krzyżowania jest większy niż prawy punkt krzyżowania! Zamieniam je miejscami.");
            int temp = _leftCrossoverPoint;
            _leftCrossoverPoint = _rightCrossoverPoint;
            _rightCrossoverPoint = temp;
        }

        for (int i = 0; i < this.boardLength; i++) {
            this.childBoardData[i] = (i < _leftCrossoverPoint || i > _rightCrossoverPoint)
                    ? this.motherBoardData[i]
                    : this.fatherBoardData[i];
        }
        return this.childBoardData;
    }

    private int[] makeMutations(int[] _boardData) {
        Random random = new Random();
        int mutatedFiledsCount = (int) this.getMutationProbability() * this.boardLength / 100;

        for (int i = 0; i < mutatedFiledsCount; i++) {
            int index = random.nextInt(this.boardLength);
            if (_boardData[index] >= 0) {
                _boardData[index] = BoardBase.EMPTY_FIELD;
            } else {
                continue;
            }
        }

        SolverGeneticParentMaker board = new SolverGeneticParentMaker(_boardData);
        board.softRandom();
        return board.getBoardData();
    }
}
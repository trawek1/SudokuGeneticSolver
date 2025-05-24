import java.util.Random;

import org.tinylog.Logger;

/**
 * UWAGI:
 * 1) przesyłane w formie jednowymiarowej tablicy dane planszy zawierają pola o
 * wartości ujemnej, które oznaczają pola stałe.
 * 2) oboje rodzice powinni mieć taki sam rozkład pól stałych - nie jest to
 * sprawdzane.
 * 3) pola o wartości 0 są polami pustymi.
 */

public class SolverGeneticCrossover {
    public static final int USE_RANDOM_POINT = -1;
    public static final int USE_SYMMETRIC_POINT = -2;
    private static final int RANDOM_BOOLEAN_BALANCE_MIN = 1;
    private static final int RANDOM_BOOLEAN_BALANCE_MAX = 20;
    private final int[] motherBoardData;
    private final int[] fatherBoardData;
    private final int boardLength;
    private int randomBooleanBalance;
    private int[] childBoardData;

    public SolverGeneticCrossover(int[] _parent1BoardData, int[] _parent2BoardData) {
        this.motherBoardData = _parent1BoardData;
        this.fatherBoardData = _parent2BoardData;
        this.boardLength = this.motherBoardData.length;
        this.childBoardData = new int[this.boardLength];
        this.randomBooleanBalance = (RANDOM_BOOLEAN_BALANCE_MIN
                + RANDOM_BOOLEAN_BALANCE_MAX) / 2;
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

    public int[] crossoverUniformBalanced() {
        for (int i = 0; i < this.boardLength; i++) {
            this.childBoardData[i] = this.getRandomBalancedBoolean()
                    ? this.motherBoardData[i]
                    : this.fatherBoardData[i];
        }
        return this.childBoardData;
    }

    public int[] crossoverSinglePoint(int _crossoverPoint) {
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

    public int[] crossoverTwoPoint(int _leftCrossoverPoint, int _rightCrossoverPoint) {
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
            Logger.warn("Lewy punkt krzyżowania jest większy niż prawy punkt krzyżowania! Zamieniam je miejscami.");
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
}
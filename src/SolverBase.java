import org.tinylog.Logger;

public class SolverBase {
    private static final int MAX_SOLVING_ITERATIONS_COUNT = 100;
    protected static final int DEFAULT_SOLVING_ITERATIONS_COUNT = 10;
    private int solvingInterationsCount = 0;
    private long solvingTimeSum = 0;
    private int solvingCounter = 0;
    private long solvingTime = 0;
    private long timeStart = 0;
    private long timeStop = 0;

    // TODO >> brak konstruktora, bo nie ma co inicjalizować

    protected void startTimeMeasurement() {
        this.solvingCounter += 1;
        this.timeStart = System.nanoTime();
    }

    protected void stopTimeMeasurement() {
        this.timeStop = System.nanoTime();
        this.solvingTime = this.timeStop - this.timeStart;
        this.solvingTimeSum += this.solvingTime;
    }

    protected void resetTimeMeasurement() {
        this.solvingTimeSum = 0;
        this.solvingCounter = 0;
        this.solvingTime = 0;
        this.timeStart = 0;
        this.timeStop = 0;
    }

    protected String showSolvingTimeAsNanoseconds() {
        return " " + this.solvingTime;
    }

    protected String showSolvingAverageTimeAsNanoseconds() {
        return " " + this.solvingTimeSum / this.solvingCounter;
    }

    private String calculateSolvingTime(long _time, int _counter) {
        long tempNanoseconds = _time / _counter;
        long minutes = tempNanoseconds / 60_000_000_000L;
        tempNanoseconds %= 60_000_000_000L;
        long seconds = tempNanoseconds / 1_000_000_000;
        tempNanoseconds %= 1_000_000_000;
        long miliseconds = tempNanoseconds / 1_000_000;
        tempNanoseconds %= 1_000_000;
        return String.format("%02dm %02ds %03dms %06dns", minutes, seconds, miliseconds, tempNanoseconds);
    }

    protected String showSolvingTime() {
        return this.calculateSolvingTime(this.solvingTime, 1);
    }

    protected String showSolvingAverageTime() {
        return this.calculateSolvingTime(this.solvingTimeSum, this.solvingCounter);
    }

    public int getSolvingIterationsCount() {
        return this.solvingInterationsCount;
    }

    public void setSolvingIterationsCount(int _count) {
        if (_count < 1 && _count > MAX_SOLVING_ITERATIONS_COUNT) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    MAX_SOLVING_ITERATIONS_COUNT, _count);
            this.solvingInterationsCount = DEFAULT_SOLVING_ITERATIONS_COUNT;
        }
        this.solvingInterationsCount = _count;
    }

}

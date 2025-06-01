import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.tinylog.Logger;

public class SolverBase {
    private static final String SOLVING_DATA_FILE_NAME = "logs/solving_info";
    private static final String SOLVING_DATA_FILE_EXT = ".csv";
    private static final int MAX_SOLVING_ITERATIONS_COUNT = 100;
    protected static final int DEFAULT_SOLVING_ITERATIONS_COUNT = 1;
    private static String solvingDataFilename = "";
    private static int solvingInterationsCount = 0;
    private static long solvingTimeSum = 0;
    private static int solvingCounter = 0;
    private static long solvingTime = 0;
    private static long timeStart = 0;
    private static long timeStop = 0;

    static {
        solvingInterationsCount = DEFAULT_SOLVING_ITERATIONS_COUNT;
        setSolvingDataFilenameToActual();
    }

    public SolverBase() {
    }

    protected static void startTimeMeasurement() {
        solvingCounter += 1;
        timeStop = 0;
        timeStart = System.nanoTime();
    }

    protected static void stopTimeMeasurement() {
        timeStop = System.nanoTime();
        solvingTime = timeStop - timeStart;
        solvingTimeSum += solvingTime;
        timeStart = 0;
        timeStop = 0;
    }

    protected static void resetTimeMeasurement() {
        solvingTimeSum = 0;
        solvingCounter = 0;
        solvingTime = 0;
        timeStart = 0;
        timeStop = 0;
    }

    protected static String showSolvingTimeAsNanoseconds() {
        return String.format("%d", solvingTime);
    }

    protected static String showSolvingAverageTimeAsNanoseconds() {
        return String.format("%d", solvingTimeSum / solvingCounter);
    }

    private static String calculateSolvingTime(long _time, int _counter) {
        long nanoseconds = _time / _counter;
        long minutes = nanoseconds / 60_000_000_000L;
        nanoseconds %= 60_000_000_000L;
        long seconds = nanoseconds / 1_000_000_000;
        nanoseconds %= 1_000_000_000;
        long miliseconds = nanoseconds / 1_000_000;
        nanoseconds %= 1_000_000;
        return String.format("%02dm %02ds %03dms %06dns", minutes, seconds, miliseconds, nanoseconds);
    }

    protected static String showSolvingTime() {
        return calculateSolvingTime(solvingTime, 1);
    }

    protected static String showSolvingAverageTime() {
        return calculateSolvingTime(solvingTimeSum, solvingCounter);
    }

    public static int getSolvingIterationsCount() {
        return solvingInterationsCount;
    }

    public static void setSolvingIterationsCount(int _count) {
        if (_count < 1 && _count > MAX_SOLVING_ITERATIONS_COUNT) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    MAX_SOLVING_ITERATIONS_COUNT, _count);
            solvingInterationsCount = DEFAULT_SOLVING_ITERATIONS_COUNT;
        }
        solvingInterationsCount = _count;
    }

    public static void setSolvingDataFilenameToActual() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_yyMMdd_HHmmss");
        solvingDataFilename = SOLVING_DATA_FILE_NAME + now.format(formatter) + SOLVING_DATA_FILE_EXT;
    }

    public static void saveSolvingDataToFile(String _dataToWrite) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(solvingDataFilename, true))) {
            writer.write(_dataToWrite);
            writer.newLine();
        } catch (IOException e) {
            Logger.warn("Błąd podczas zapisu do pliku '{}'! Error: {}", solvingDataFilename, e.getMessage());
        }
    }
}

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.tinylog.Logger;

public class SolverBase {
    private static final int FILENAME_APPENDIX_MAX_LENGTH = 25;
    private static final String SOLVING_DATA_FILE_NAME = "logs/dane";
    private static final String SOLVING_DATA_FILE_EXT = ".csv";
    private static final int MAX_SOLVING_ITERATIONS_COUNT = 100;
    protected static final int DEFAULT_SOLVING_ITERATIONS_COUNT = 1;
    protected static final long SOLVING_TIME_MAX = 1_800_000; // 600_000 = 10 minutes in milliseconds
    private static String solvingDataFilename = "";
    private static int solvingInterationsLimit = 0;
    private static long solvingTimeSum = 0;
    private static int solvingIterationsCounter = 0;
    private static long solvingTime = 0;
    private static long timeStart = 0;
    private static long timeStop = 0;

    static {
        solvingInterationsLimit = DEFAULT_SOLVING_ITERATIONS_COUNT;
    }

    public SolverBase() {
    }

    public static String convertMilisecondsToHMSV(long _miliseconds, int _precision) {
        long miliseconds = _miliseconds;
        String result = "";
        if (_precision < 1) {
            _precision = 10;
        }

        long time = miliseconds / 3_600_000;
        miliseconds %= 3_600_000;
        result += String.format("%02dh", time);
        if (_precision == 1) {
            return result;
        }

        time = miliseconds / 60_000;
        miliseconds %= 60_000;
        result += String.format(" %02dm", time);
        if (_precision == 2) {
            return result;
        }

        time = miliseconds / 1_000;
        miliseconds %= 1_000;
        result += String.format(" %02ds", time);
        if (_precision == 3) {
            return result;
        }

        return result + String.format(" %03dms", miliseconds);
    }

    public static String convertNanosecondsToHMSVU(long _nanoseconds, int _precision) {
        long nanoseconds = _nanoseconds;
        String result = "";
        if (_precision < 1) {
            _precision = 10;
        }

        long time = nanoseconds / 3_600_000_000_000L;
        nanoseconds %= 3_600_000_000_000L;
        result += String.format("%02dh", time);
        if (_precision == 1) {
            return result;
        }

        time = nanoseconds / 60_000_000_000L;
        nanoseconds %= 60_000_000_000L;
        result += String.format(" %02dm", time);
        if (_precision == 2) {
            return result;
        }

        time = nanoseconds / 1_000_000_000;
        nanoseconds %= 1_000_000_000;
        result += String.format(" %02ds", time);
        if (_precision == 3) {
            return result;
        }

        time = nanoseconds / 1_000_000;
        nanoseconds %= 1_000_000;
        result += String.format(" %03dms", time);
        if (_precision == 4) {
            return result;
        }

        return result + String.format(" %06dns", nanoseconds);
    }

    protected static void startTimeMeasurement() {
        solvingIterationsCounter += 1;
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
        solvingIterationsCounter = 0;
        solvingTime = 0;
        timeStart = 0;
        timeStop = 0;
    }

    protected static String showSolvingTimeAsNanoseconds() {
        return String.format("%d", solvingTime);
    }

    protected static String showSolvingAverageTimeAsNanoseconds() {
        return String.format("%d", solvingTimeSum / solvingIterationsCounter);
    }

    private static String calculateSolvingTime(long _time, int _counter) {
        long nanoseconds = _time / _counter;
        return convertNanosecondsToHMSVU(nanoseconds, 5);
    }

    protected static String showSolvingTime() {
        return calculateSolvingTime(solvingTime, 1);
    }

    protected static String showSolvingAverageTime() {
        return calculateSolvingTime(solvingTimeSum, solvingIterationsCounter);
    }

    public static int getSolvingIterationsLimit() {
        return solvingInterationsLimit;
    }

    public static int getSolvingIterationsCounter() {
        return solvingIterationsCounter;
    }

    public static boolean areIterationsInProgress() {
        return (solvingIterationsCounter < solvingInterationsLimit);
    }

    public static boolean isSolvingIterationsCountInRange(int _count) {
        return (_count >= 1 && _count <= MAX_SOLVING_ITERATIONS_COUNT);
    }

    public static boolean setSolvingIterationsLimit(int _count) {
        if (!isSolvingIterationsCountInRange(_count)) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    MAX_SOLVING_ITERATIONS_COUNT, _count);
            return false;
        }
        solvingInterationsLimit = _count;
        return true;
    }

    public static void setSolvingDataFilenameToActual(String _filenameAppendix) {
        if (_filenameAppendix == null) {
            _filenameAppendix = "";
        }
        if (_filenameAppendix.length() > FILENAME_APPENDIX_MAX_LENGTH) {
            _filenameAppendix = _filenameAppendix.substring(0, FILENAME_APPENDIX_MAX_LENGTH);
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("-yyMMdd-HHmmss-");
        solvingDataFilename = SOLVING_DATA_FILE_NAME + now.format(formatter) + _filenameAppendix
                + SOLVING_DATA_FILE_EXT;
    }

    public static void saveSolvingDataToFile(String _dataToWrite) {
        if (solvingDataFilename == "") {
            Logger.info("Nie zapisano danych: " + _dataToWrite);
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(solvingDataFilename, true))) {
            writer.write(_dataToWrite);
            writer.newLine();
        } catch (IOException e) {
            Logger.warn("Błąd podczas zapisu do pliku '{}'! Error: {}", solvingDataFilename, e.getMessage());
        }
    }

    public static boolean isSolvingIterationsEnded() {
        if (solvingIterationsCounter >= solvingInterationsLimit) {
            return true;
        }
        return false;
    }
}

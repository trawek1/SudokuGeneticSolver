import org.tinylog.Logger;

public class App {
    public static void main(String[] args) {
        System.out.println("==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ====");
        System.out.println("==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== === START");
        Logger.debug("==== ==== ==== ==== START");

        Logger.debug("==== ==== ==== ==== STOP");
        System.out.println("==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== STOP");
        System.out.println("==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ==== ====");
    }
}

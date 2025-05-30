public enum SolvingMethods {
    BRUTE_FORCE("rekurencyjna") {
        public SolvingMethods next() {
            return GENETIC;
        }
    },
    GENETIC("algorytmów genetycznych") {
        public SolvingMethods next() {
            return BRUTE_FORCE;
        }
    };

    private final String displayName;

    public abstract SolvingMethods next();

    SolvingMethods(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

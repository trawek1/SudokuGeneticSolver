public enum MethodsOfCrossover {
    CROSSOVER_UNIFORM("losowa") {
        public MethodsOfCrossover next() {
            return CROSSOVER_BALANCED_UNIFORM;
        }
    },
    CROSSOVER_BALANCED_UNIFORM("losowa balansowana") {
        public MethodsOfCrossover next() {
            return CROSSOVER_SINGLE_POINT;
        }
    },
    CROSSOVER_SINGLE_POINT("jedno-punktowa") {
        public MethodsOfCrossover next() {
            return CROSSOVER_TWO_POINTS;
        }
    },
    CROSSOVER_TWO_POINTS("dwu-punktowa") {
        public MethodsOfCrossover next() {
            return CROSSOVER_UNIFORM;
        }
    };

    private final String displayName;

    public abstract MethodsOfCrossover next();

    MethodsOfCrossover(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
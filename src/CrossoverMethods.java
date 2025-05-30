public enum CrossoverMethods {
    CROSSOVER_UNIFORM("losowa") {
        public CrossoverMethods next() {
            return CROSSOVER_BALANCED_UNIFORM;
        }
    },
    CROSSOVER_BALANCED_UNIFORM("losowa balansowana") {
        public CrossoverMethods next() {
            return CROSSOVER_SINGLE_POINT;
        }
    },
    CROSSOVER_SINGLE_POINT("jedno-punktowa") {
        public CrossoverMethods next() {
            return CROSSOVER_TWO_POINTS;
        }
    },
    CROSSOVER_TWO_POINTS("dwu-punktowa") {
        public CrossoverMethods next() {
            return CROSSOVER_UNIFORM;
        }
    };

    private final String displayName;

    public abstract CrossoverMethods next();

    CrossoverMethods(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
public enum CrossoverMethodsEnum {
    CROSSOVER_UNIFORM("losowa") {
        public CrossoverMethodsEnum next() {
            return CROSSOVER_BALANCED_UNIFORM;
        }
    },
    CROSSOVER_BALANCED_UNIFORM("losowa balansowana") {
        public CrossoverMethodsEnum next() {
            return CROSSOVER_SINGLE_POINT;
        }
    },
    CROSSOVER_SINGLE_POINT("jedno-punktowa") {
        public CrossoverMethodsEnum next() {
            return CROSSOVER_TWO_POINTS;
        }
    },
    CROSSOVER_TWO_POINTS("dwu-punktowa") {
        public CrossoverMethodsEnum next() {
            return CROSSOVER_UNIFORM;
        }
    };

    private final String displayName;

    public abstract CrossoverMethodsEnum next();

    CrossoverMethodsEnum(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
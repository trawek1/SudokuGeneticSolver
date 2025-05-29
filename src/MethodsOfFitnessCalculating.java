public enum MethodsOfFitnessCalculating {
    MISSING_VALUES("liczy brakujące") {
        public MethodsOfFitnessCalculating next() {
            return VALUES_COLLISIONS;
        }
    },
    VALUES_COLLISIONS("liczy kolizje") {
        public MethodsOfFitnessCalculating next() {
            return MISSING_VALUES;
        }
    };

    private final String displayName;

    public abstract MethodsOfFitnessCalculating next();

    MethodsOfFitnessCalculating(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

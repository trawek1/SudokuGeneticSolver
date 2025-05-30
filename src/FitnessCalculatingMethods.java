public enum FitnessCalculatingMethods {
    MISSING_VALUES("liczy brakujące") {
        public FitnessCalculatingMethods next() {
            return VALUES_COLLISIONS;
        }
    },
    VALUES_COLLISIONS("liczy kolizje") {
        public FitnessCalculatingMethods next() {
            return MISSING_VALUES;
        }
    };

    private final String displayName;

    public abstract FitnessCalculatingMethods next();

    FitnessCalculatingMethods(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

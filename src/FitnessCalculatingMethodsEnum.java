public enum FitnessCalculatingMethodsEnum {
    MISSING_VALUES("liczy brakujące") {
        public FitnessCalculatingMethodsEnum next() {
            return VALUES_COLLISIONS;
        }
    },
    VALUES_COLLISIONS("liczy kolizje") {
        public FitnessCalculatingMethodsEnum next() {
            return MISSING_VALUES;
        }
    };

    private final String displayName;

    public abstract FitnessCalculatingMethodsEnum next();

    FitnessCalculatingMethodsEnum(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

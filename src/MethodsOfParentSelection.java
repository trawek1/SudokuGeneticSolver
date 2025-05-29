public enum MethodsOfParentSelection {
    ELITE_SELECTION("elitarna") {
        public MethodsOfParentSelection next() {
            return ROULETTE_SELECTION;
        }
    },
    ROULETTE_SELECTION("ruletkowa") {
        public MethodsOfParentSelection next() {
            return ELITE_SELECTION;
        }
    };

    private final String displayName;

    public abstract MethodsOfParentSelection next();

    MethodsOfParentSelection(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

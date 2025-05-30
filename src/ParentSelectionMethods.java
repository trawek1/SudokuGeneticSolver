public enum ParentSelectionMethods {
    ELITE_SELECTION("elitarna") {
        public ParentSelectionMethods next() {
            return ROULETTE_SELECTION;
        }
    },
    ROULETTE_SELECTION("ruletkowa") {
        public ParentSelectionMethods next() {
            return ELITE_SELECTION;
        }
    };

    private final String displayName;

    public abstract ParentSelectionMethods next();

    ParentSelectionMethods(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

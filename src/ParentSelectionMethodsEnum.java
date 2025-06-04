public enum ParentSelectionMethodsEnum {
    ELITE_SELECTION("elitarna") {
        public ParentSelectionMethodsEnum next() {
            return ROULETTE_SELECTION;
        }
    },
    ROULETTE_SELECTION("ruletkowa") {
        public ParentSelectionMethodsEnum next() {
            return ELITE_SELECTION;
        }
    };

    private final String displayName;

    public abstract ParentSelectionMethodsEnum next();

    ParentSelectionMethodsEnum(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

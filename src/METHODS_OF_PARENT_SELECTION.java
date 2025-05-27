public enum METHODS_OF_PARENT_SELECTION {
    ELITE_SELECTION("Selekcja elitarna"),
    ROULETTE_SELECTION("Selekcja ruletkowa");

    private final String displayName;

    METHODS_OF_PARENT_SELECTION(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

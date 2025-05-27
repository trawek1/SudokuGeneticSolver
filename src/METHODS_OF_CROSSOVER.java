public enum METHODS_OF_CROSSOVER {
    CROSSOVER_UNIFORM("Krzyżowanie losowe"),
    CROSSOVER_BALANCED_UNIFORM("Krzyżowanie losowe balansowane"),
    CROSSOVER_SINGLE_POINT("Krzyżowanie jedno-punktowe"),
    CROSSOVER_TWO_POINTS("Krzyżowanie dwu-punktowe");

    private final String displayName;

    METHODS_OF_CROSSOVER(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
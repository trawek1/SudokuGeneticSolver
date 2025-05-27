public enum METHODS_OF_FITNESS_CALCULATING {
    VALUES_MISSING("Ilość brakujące wartości w wierszu, kolumnie i bloku"),
    VALUES_COLLISIONS("Ilość kolizji wartości w wierszu, kolumnie i bloku");

    private final String displayName;

    METHODS_OF_FITNESS_CALCULATING(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

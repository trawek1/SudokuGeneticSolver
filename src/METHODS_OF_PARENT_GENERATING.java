public enum METHODS_OF_PARENT_GENERATING {
    RANDOM_FULL("Generowanie w pełni losowe"),
    RANDOM_WITH_CHECKING("Generowanie losowe ze sprawdzaniem");

    private final String displayName;

    METHODS_OF_PARENT_GENERATING(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

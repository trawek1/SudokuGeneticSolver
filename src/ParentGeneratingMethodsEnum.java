public enum ParentGeneratingMethodsEnum {
    RANDOM_FULL("losowy bez kontroli") {
        public ParentGeneratingMethodsEnum next() {
            return RANDOM_WITH_CHECKING;
        }
    },
    RANDOM_WITH_CHECKING("losowy ze sprawdzaniem") {
        public ParentGeneratingMethodsEnum next() {
            return RANDOM_FULL;
        }
    };

    private final String displayName;

    public abstract ParentGeneratingMethodsEnum next();

    ParentGeneratingMethodsEnum(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

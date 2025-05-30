public enum ParentGeneratingMethods {
    RANDOM_FULL("losowy bez kontroli") {
        public ParentGeneratingMethods next() {
            return RANDOM_WITH_CHECKING;
        }
    },
    RANDOM_WITH_CHECKING("losowy ze sprawdzaniem") {
        public ParentGeneratingMethods next() {
            return RANDOM_FULL;
        }
    };

    private final String displayName;

    public abstract ParentGeneratingMethods next();

    ParentGeneratingMethods(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

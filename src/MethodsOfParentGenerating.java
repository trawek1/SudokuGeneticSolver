public enum MethodsOfParentGenerating {
    RANDOM_FULL("losowy bez kontroli") {
        public MethodsOfParentGenerating next() {
            return RANDOM_WITH_CHECKING;
        }
    },
    RANDOM_WITH_CHECKING("losowy ze sprawdzaniem") {
        public MethodsOfParentGenerating next() {
            return RANDOM_FULL;
        }
    };

    private final String displayName;

    public abstract MethodsOfParentGenerating next();

    MethodsOfParentGenerating(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

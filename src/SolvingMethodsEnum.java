public enum SolvingMethodsEnum {
    BRUTE_FORCE_X01("brute force (x1)") {
        public SolvingMethodsEnum next() {
            return BRUTE_FORCE_X03;
        }
    },
    BRUTE_FORCE_X03("brute force : (x3)") {
        public SolvingMethodsEnum next() {
            return BRUTE_FORCE_X10;
        }
    },
    BRUTE_FORCE_X10("brute force (x10)") {
        public SolvingMethodsEnum next() {
            return GENETIC_X01;
        }
    },
    GENETIC_X01("alg. genetyczne (x1)") {
        public SolvingMethodsEnum next() {
            return GENETIC_X03;
        }
    },
    GENETIC_X03("alg. genetyczne (x3)") {
        public SolvingMethodsEnum next() {
            return GENETIC_X10;
        }
    },
    GENETIC_X10("alg. genetyczne (x10)") {
        public SolvingMethodsEnum next() {
            return BRUTE_FORCE_X01;
        }
    };

    private final String displayName;

    public abstract SolvingMethodsEnum next();

    SolvingMethodsEnum(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

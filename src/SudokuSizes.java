public enum SudokuSizes {
    X2("2x2") {
        public SudokuSizes next() {
            return X3;
        }
    },
    X3("3x3") {
        public SudokuSizes next() {
            return X4;
        }
    },
    X4("4x4") {
        public SudokuSizes next() {
            return X5;
        }
    },
    X5("5x5") {
        public SudokuSizes next() {
            return X2;
        }
    };

    private final String displayName;

    public abstract SudokuSizes next();

    SudokuSizes(String _displayName) {
        this.displayName = _displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
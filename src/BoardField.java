import org.tinylog.Logger;

public class BoardField extends BoardBase {

    private int value;
    private boolean isConst;
    protected BoardFieldPossibilities possibilities;

    public BoardField(int _sudokuSize) {
        super(_sudokuSize);
        this.value = EMPTY_FIELD;
        this.isConst = false;
        this.possibilities = new BoardFieldPossibilities(_sudokuSize);
        this.possibilities.setAllValuesAsUnknown();
    }

    public BoardField(int _sudokuSize, int _constValue) {
        this(_sudokuSize);
        if (!this.isValueInRange(_constValue)) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    _constValue, this.getMaxValue());
            return;
        }
        this.value = _constValue;
        this.isConst = true;
        if (this.arePossibilitiesCheckingOn()) {
            this.possibilities.setAllValuesAsImpossible();
        }
    }

    public boolean isConstField() {
        return this.isConst;
    }

    public int getValue() {
        return this.value;
    }

    public boolean setValue(int _value) {
        if (this.isConst) {
            Logger.warn("Próbowano zmienić wartość pola stałego!");
            return false;
        }
        if (!this.isValueInRange(_value)) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    _value, this.getMaxValue());
            return false;
        }
        if (this.arePossibilitiesCheckingOn()) {
            if (this.possibilities.isValueImpossible(_value)) {
                return false;
            }
        }
        this.value = _value;
        if (this.arePossibilitiesCheckingOn()) {
            this.possibilities.setAllValuesAsImpossible();
        }
        return true;
    }

    public boolean removeValue() {
        if (this.isConst) {
            Logger.warn("Próbowano zmienić wartość pola stałego!");
            return false;
        }
        this.value = EMPTY_FIELD;
        if (this.arePossibilitiesCheckingOn()) {
            this.possibilities.setAllValuesAsUnknown();
        }
        return true;
    }

}

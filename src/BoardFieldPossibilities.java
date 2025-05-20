import org.tinylog.Logger;

import java.util.HashSet;
import java.util.Set;

public class BoardFieldPossibilities extends BoardBase {

    private Set<Integer> unknownValues;
    private Set<Integer> possibleValues;
    private Set<Integer> impossibleValues;

    public BoardFieldPossibilities(int _sudokuSize) {
        super(_sudokuSize);
        this.unknownValues = new HashSet<>();
        this.possibleValues = new HashSet<>();
        this.impossibleValues = new HashSet<>();
        this.setAllValuesAsUnknown();
    }

    public void setValueAsUnknown(int _value) {
        if (_value < 1 || _value > this.getBoardSize()) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    _value, this.getMaxValue());
            return;
        }
        this.unknownValues.add(_value);
        this.possibleValues.remove(_value);
        this.impossibleValues.remove(_value);
    }

    public void setValueAsPossible(int _value) {
        if (_value < 1 || _value > this.getBoardSize()) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    _value, this.getMaxValue());
            return;
        }
        this.unknownValues.remove(_value);
        this.possibleValues.add(_value);
        this.impossibleValues.remove(_value);
    }

    public void setValueAsImpossible(int _value) {
        if (_value < 1 || _value > this.getBoardSize()) {
            Logger.warn("Wartość jest spoza zakresu! Oczekiwano 1-{}, otrzymano {}.",
                    _value, this.getMaxValue());
            return;
        }
        this.unknownValues.remove(_value);
        this.possibleValues.remove(_value);
        this.impossibleValues.add(_value);
    }

    public void setManyValuesAsImpossible(Set<Integer> _usedValues) {
        for (Integer usedValue : _usedValues) {
            if (usedValue != null) {
                if (usedValue != 0) {
                    int value = (int) Math.abs(usedValue);
                    this.setValueAsImpossible(value);
                }
            } else {
                Logger.warn("Nieoczekiwana wartość typu null!");
            }
        }
    }

    public void setAllValuesAsUnknown() {
        for (int i = 1; i <= this.getBoardSize(); i++) {
            this.setValueAsUnknown(i);
        }
    }

    public void setAllValuesAsPossible() {
        for (int i = 1; i <= this.getBoardSize(); i++) {
            this.setValueAsPossible(i);
        }
    }

    public void setAllValuesAsImpossible() {
        for (int i = 1; i <= this.getBoardSize(); i++) {
            this.setValueAsImpossible(i);
        }
    }

    public boolean isValueUnknown(int _value) {
        return this.unknownValues.contains(_value);
    }

    public boolean isValuePossible(int _value) {
        return this.possibleValues.contains(_value);
    }

    public boolean isValueImpossible(int _value) {
        return this.impossibleValues.contains(_value);
    }

    public void clearValuesUnknown() {
        this.unknownValues.clear();
    }

    public void clearValuesPossible() {
        this.possibleValues.clear();
    }

    public void clearValuesImpossible() {
        this.impossibleValues.clear();
    }

    public int[] getValuesUnknown() {
        int[] result = new int[this.unknownValues.size()];
        int i = 0;
        for (Integer value : this.unknownValues) {
            result[i++] = (value == null ? EMPTY_FIELD : (int) value);
        }
        return result;
    }

    public int[] getValuesPossible() {
        int[] result = new int[this.possibleValues.size()];
        int i = 0;
        for (Integer value : this.possibleValues) {
            result[i++] = (value == null ? EMPTY_FIELD : (int) value);
        }
        return result;
    }

    public int[] getValuesImpossible() {
        int[] result = new int[this.impossibleValues.size()];
        int i = 0;
        for (Integer value : this.impossibleValues) {
            result[i++] = (value == null ? EMPTY_FIELD : (int) value);
        }
        return result;
    }

}

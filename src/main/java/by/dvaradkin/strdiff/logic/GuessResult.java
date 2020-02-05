package by.dvaradkin.strdiff.logic;

public class GuessResult {
    private final String originalValue;
    private final String resultingValue;

    public String getOriginalValue() {
        return originalValue;
    }

    public String getResultingValue() {
        return resultingValue;
    }

    public GuessResult(String originalValue, String resultingValue) {
        this.originalValue = originalValue;
        this.resultingValue = resultingValue;
    }

    @Override
    public String toString() {
        return originalValue + " -> " + resultingValue;
    }
}
